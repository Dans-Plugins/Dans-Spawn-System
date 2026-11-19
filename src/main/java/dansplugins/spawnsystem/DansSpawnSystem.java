package dansplugins.spawnsystem;

import dansplugins.spawnsystem.bstats.Metrics;
import dansplugins.spawnsystem.config.ConfigManager;
import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.services.CommandService;
import dansplugins.spawnsystem.services.StorageService;
import dansplugins.spawnsystem.trace.TraceClient;
import dansplugins.spawnsystem.trace.UsageReportingSetting;
import dansplugins.spawnsystem.utils.BlockChecker;
import dansplugins.spawnsystem.utils.EventRegistry;
import dansplugins.spawnsystem.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;

public final class DansSpawnSystem extends JavaPlugin implements Listener {
    private final BlockChecker blockChecker = new BlockChecker();
    private final PersistentData persistentData = new PersistentData();
    private final EventRegistry eventRegistry = new EventRegistry(this, blockChecker, persistentData);
    private final StorageService storageService = new StorageService(this, persistentData);
    private final UUIDChecker uuidChecker = new UUIDChecker();
    private final ConfigManager configManager = new ConfigManager(this);

    // A no-op until the config has been read, so a command arriving before
    // onEnable() finishes has something safe to report to.
    private TraceClient trace = TraceClient.disabled();

    @Override
    public void onEnable() {
        // load config
        configManager.saveDefaultConfig();

        // register events
        eventRegistry.registerEvents();

        // load spawns
        storageService.load();

        // bStats
        int pluginId = 12161;
        Metrics metrics = new Metrics(this, pluginId);

        // usage reporting: one event now, one per command; see config.yml
        configManager.saveUsageReportingDefaultsIfMissing();
        trace = TraceClient.builder(configManager.getUsageReportingEndpoint(), getName(), getDescription().getVersion())
                .key(configManager.getUsageReportingKey())
                .enabled(configManager.isUsageReportingEnabled())
                .serverWideConfig(getDataFolder().getParentFile())
                .logger(getLogger())
                .build();
        logUsageReportingStatus();
        trace.report("startup");
    }

    /** Says on every start whether usage reporting is on, and why not when it is off. */
    private void logUsageReportingStatus() {
        if (trace.isEnabled()) {
            getLogger().info("Usage reporting is on: " + getName() + " sends its name, version and command names to "
                    + configManager.getUsageReportingEndpoint() + ", plus a random server ID (server-id in plugins/trace/config.yml) - nothing about players. "
                    + "Turn it off with usage-reporting.enabled: false in this plugin's config.yml, "
                    + "or for every plugin with enabled: false in plugins/trace/config.yml. "
                    + "Details: https://danielstephenson.dev/usage-reporting");
        } else {
            getLogger().info("Usage reporting is off (" + trace.disabledReason() + ").");
        }
    }

    /**
     * Every save of config.yml goes through here. A file Bukkit could not parse was loaded as
     * empty, so saving would replace it with the bundled defaults, usage reporting switched on
     * included; it is left alone instead. An opt-out written into the file by hand while the
     * server runs is carried into the loaded copy first, so a save cannot turn reporting back on.
     */
    @Override
    public void saveConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (UsageReportingSetting.isUnparseable(configFile)) {
            getLogger().severe("config.yml could not be read, so it was not overwritten. Fix the YAML error in it and restart; "
                    + "until then the defaults are used, and usage reporting is off.");
            return;
        }
        UsageReportingSetting.keepOptOutFromDisk(getConfig(), configFile);
        super.saveConfig();
    }

    @Override
    public void onDisable() {
        trace.close();
        storageService.save();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Console, command-block and plugin-dispatched commands are not counted.
        if (sender instanceof Player) {
            trace.report("command", null, Collections.singletonMap("name", cmd.getName()));
        }
        CommandService localCommandService = new CommandService(persistentData, uuidChecker, getServer());
        return localCommandService.interpretCommand(sender, cmd, args);
    }
}