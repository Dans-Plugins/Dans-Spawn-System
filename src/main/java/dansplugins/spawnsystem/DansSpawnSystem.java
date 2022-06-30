package dansplugins.spawnsystem;

import dansplugins.spawnsystem.bstats.Metrics;
import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.services.CommandService;
import dansplugins.spawnsystem.services.StorageService;
import dansplugins.spawnsystem.utils.BlockChecker;
import dansplugins.spawnsystem.utils.EventRegistry;
import dansplugins.spawnsystem.utils.UUIDChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class DansSpawnSystem extends JavaPlugin implements Listener {
    private final BlockChecker blockChecker = new BlockChecker();
    private final PersistentData persistentData = new PersistentData();
    private final EventRegistry eventRegistry = new EventRegistry(this, blockChecker, persistentData);
    private final StorageService storageService = new StorageService(this, persistentData);
    private final UUIDChecker uuidChecker = new UUIDChecker();

    @Override
    public void onEnable() {
        // register events
        eventRegistry.registerEvents();

        // load spawns
        storageService.load();

        // bStats
        int pluginId = 12161;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        storageService.save();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandService localCommandService = new CommandService(persistentData, uuidChecker, this);
        return localCommandService.interpretCommand(sender, label, args);
    }
}