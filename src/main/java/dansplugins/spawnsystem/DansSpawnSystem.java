package dansplugins.spawnsystem;

import dansplugins.spawnsystem.bstats.Metrics;
import dansplugins.spawnsystem.config.ConfigManager;
import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.services.CommandService;
import dansplugins.spawnsystem.services.StorageService;
import dansplugins.spawnsystem.trace.TraceClient;
import dansplugins.spawnsystem.utils.BlockChecker;
import dansplugins.spawnsystem.utils.EventRegistry;
import dansplugins.spawnsystem.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
        trace = TraceClient.builder(configManager.getUsageReportingEndpoint(), getName())
                .key(configManager.getUsageReportingKey())
                .enabled(configManager.isUsageReportingEnabled())
                .logger(getLogger())
                .build();
        trace.report("startup", null, Collections.singletonMap("version", getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        trace.close();
        storageService.save();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        trace.report("command", null, Collections.singletonMap("name", cmd.getName()));
        CommandService localCommandService = new CommandService(persistentData, uuidChecker, this);
        return localCommandService.interpretCommand(sender, label, args);
    }
}