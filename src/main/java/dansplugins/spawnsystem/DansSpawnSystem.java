package dansplugins.spawnsystem;

import dansplugins.spawnsystem.bstats.Metrics;
import dansplugins.spawnsystem.eventhandlers.*;
import dansplugins.spawnsystem.managers.StorageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
        StorageManager.getInstance().load();

        // bStats
        int pluginId = 12161;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onDisable() {
        StorageManager.getInstance().save();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandInterpreter commandInterpreter = new CommandInterpreter();
        return commandInterpreter.interpretCommand(sender, label, args);
    }
}