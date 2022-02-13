package dansplugins.spawnsystem.utils;

import dansplugins.spawnsystem.DansSpawnSystem;
import dansplugins.spawnsystem.eventhandlers.*;
import org.bukkit.plugin.PluginManager;

public class EventRegistry {

    private static EventRegistry instance;

    private EventRegistry() {

    }

    public static EventRegistry getInstance() {
        if (instance == null) {
            instance = new EventRegistry();
        }
        return instance;
    }

    public void registerEvents() {

        DansSpawnSystem mainInstance = DansSpawnSystem.getInstance();
        PluginManager manager = mainInstance.getServer().getPluginManager();

        // event handlers
        manager.registerEvents(new BlockBreakEventHandler(), mainInstance);
        manager.registerEvents(new PlayerDeathEventHandler(), mainInstance);
        manager.registerEvents(new PlayerInteractEventHandler(), mainInstance);
        manager.registerEvents(new PlayerRespawnEventHandler(), mainInstance);
        manager.registerEvents(new SignChangeEventHandler(), mainInstance);
    }

}
