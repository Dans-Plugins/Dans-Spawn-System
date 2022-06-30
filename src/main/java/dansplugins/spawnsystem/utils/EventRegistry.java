package dansplugins.spawnsystem.utils;

import dansplugins.spawnsystem.DansSpawnSystem;
import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.listeners.*;
import org.bukkit.plugin.PluginManager;

public class EventRegistry {
    private final DansSpawnSystem dansSpawnSystem;
    private final BlockChecker blockChecker;
    private final PersistentData persistentData;

    public EventRegistry(DansSpawnSystem dansSpawnSystem, BlockChecker blockChecker, PersistentData persistentData) {
        this.dansSpawnSystem = dansSpawnSystem;
        this.blockChecker = blockChecker;
        this.persistentData = persistentData;
    }

    public void registerEvents() {
        PluginManager manager = dansSpawnSystem.getServer().getPluginManager();

        // event handlers
        manager.registerEvents(new BlockBreakListener(blockChecker), dansSpawnSystem);
        manager.registerEvents(new PlayerDeathListener(), dansSpawnSystem);
        manager.registerEvents(new PlayerInteractListener(blockChecker, persistentData), dansSpawnSystem);
        manager.registerEvents(new PlayerRespawnListener(persistentData, dansSpawnSystem), dansSpawnSystem);
        manager.registerEvents(new SignChangeEventListener(), dansSpawnSystem);
    }

}
