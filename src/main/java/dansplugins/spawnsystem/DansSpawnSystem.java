package dansplugins.spawnsystem;

import org.bukkit.Location;
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
import dansplugins.spawnsystem.eventhandlers.*;
import dansplugins.spawnsystem.managers.StorageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class DansSpawnSystem extends JavaPlugin implements Listener {

    private static DansSpawnSystem instance;

    public static DansSpawnSystem getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(this, this);

        StorageManager.getInstance().load();
    }

    @Override
    public void onDisable() {
        StorageManager.getInstance().save();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        CommandInterpreter commandInterpreter = new CommandInterpreter();
        return commandInterpreter.interpretCommand(sender, label, args);
    }

    @EventHandler()
    public void onDeath(PlayerDeathEvent event) {
        PlayerDeathEventHandler handler = new PlayerDeathEventHandler();
        handler.handle(event);
    }

    @EventHandler()
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerRespawnEventHandler handler = new PlayerRespawnEventHandler();
        handler.handle(event);
    }

    @EventHandler()
    public void onSignChange(SignChangeEvent event) {
        SignChangeEventHandler handler = new SignChangeEventHandler();
        handler.handle(event);
    }

    @EventHandler()
    public void onRightClick(PlayerInteractEvent event) {
        PlayerInteractEventHandler handler = new PlayerInteractEventHandler();
        handler.handle(event);
    }

    @EventHandler()
    public void onBlockBreak(BlockBreakEvent event) {
        BlockBreakEventHandler handler = new BlockBreakEventHandler();
        handler.handle(event);
    }
}