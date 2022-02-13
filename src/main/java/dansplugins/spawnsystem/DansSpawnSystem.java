package dansplugins.spawnsystem;

import dansplugins.spawnsystem.bstats.Metrics;
import dansplugins.spawnsystem.services.LocalCommandService;
import dansplugins.spawnsystem.services.LocalStorageService;
import dansplugins.spawnsystem.utils.EventRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class DansSpawnSystem extends JavaPlugin implements Listener {

    private static DansSpawnSystem instance;

    private final String version = "v1.1";

    public static DansSpawnSystem getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // register events
        EventRegistry.getInstance().registerEvents();

        // load spawns
        LocalStorageService.getInstance().load();

        // bStats
        int pluginId = 12161;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        LocalStorageService.getInstance().save();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        LocalCommandService localCommandService = new LocalCommandService();
        return localCommandService.interpretCommand(sender, label, args);
    }
}