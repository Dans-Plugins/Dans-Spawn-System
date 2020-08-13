package spawnsystem;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import spawnsystem.EventHandlers.*;
import spawnsystem.Subsystems.CommandSubsystem;
import spawnsystem.Subsystems.StorageSubsystem;
import spawnsystem.Subsystems.UtilitySubsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin implements Listener {

    // subsystems
    public CommandSubsystem commands = new CommandSubsystem(this);
    public UtilitySubsystem utilities = new UtilitySubsystem(this);
    public StorageSubsystem storage = new StorageSubsystem(this);

    // saved
    public HashMap<UUID, Location> playerSpawns = new HashMap<>();
    public ArrayList<UUID> playersWithSpawns = new ArrayList<>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        storage.load();
    }

    @Override
    public void onDisable() {
        storage.save();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return commands.interpretCommand(sender, label, args);
    }

    @EventHandler()
    public void onDeath(PlayerDeathEvent event) {
        PlayerDeathEventHandler handler = new PlayerDeathEventHandler();
        handler.handle(event);
    }

    @EventHandler()
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerRespawnEventHandler handler = new PlayerRespawnEventHandler(this);
        handler.handle(event);
    }

    @EventHandler()
    public void onSignChange(SignChangeEvent event) {
        SignChangeEventHandler handler = new SignChangeEventHandler(this);
        handler.handle(event);
    }

    @EventHandler()
    public void onRightClick(PlayerInteractEvent event) {
        PlayerInteractEventHandler handler = new PlayerInteractEventHandler(this);
        handler.handle(event);
    }

    @EventHandler()
    public void onBlockBreak(BlockBreakEvent event) {
        BlockBreakEventHandler handler = new BlockBreakEventHandler(this);
        handler.handle(event);
    }
}