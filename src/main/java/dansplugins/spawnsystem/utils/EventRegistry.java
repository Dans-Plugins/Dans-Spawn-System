package dansplugins.spawnsystem.utils;

import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.listeners.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class EventRegistry {
    private final Plugin plugin;
    private final BlockChecker blockChecker;
    private final PersistentData persistentData;

    public EventRegistry(Plugin plugin, BlockChecker blockChecker, PersistentData persistentData) {
        this.plugin = plugin;
        this.blockChecker = blockChecker;
        this.persistentData = persistentData;
    }

    public void registerEvents() {
        PluginManager manager = plugin.getServer().getPluginManager();

        // event handlers
        manager.registerEvents(new BlockBreakListener(blockChecker), plugin);
        manager.registerEvents(new PlayerDeathListener(), plugin);
        manager.registerEvents(new PlayerInteractListener(blockChecker, persistentData, plugin.getLogger()), plugin);
        manager.registerEvents(new PlayerRespawnListener(persistentData, plugin), plugin);
        manager.registerEvents(new SignChangeEventListener(), plugin);
    }

}
