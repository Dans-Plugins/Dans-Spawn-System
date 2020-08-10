package spawnsystem;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import spawnsystem.EventHandlers.SignChangeEventHandler;
import spawnsystem.EventHandlers.PlayerDeathEventHandler;
import spawnsystem.EventHandlers.PlayerInteractEventHandler;
import spawnsystem.EventHandlers.PlayerRespawnEventHandler;
import spawnsystem.Subsystems.CommandSubsystem;
import spawnsystem.Subsystems.StorageSubsystem;
import spawnsystem.Subsystems.UtilitySubsystem;

import java.util.ArrayList;
import java.util.HashMap;

public final class Main extends JavaPlugin implements Listener {

    // subsystems
    public CommandSubsystem commands = new CommandSubsystem(this);
    public UtilitySubsystem utilities = new UtilitySubsystem(this);
    public StorageSubsystem storage = new StorageSubsystem(this);

    // saved
    public HashMap<String, Location> playerSpawns = new HashMap<>();
    public ArrayList<String> playersWithSpawns = new ArrayList<>();

    // temporary
    public String[] subcultures = {"Ostendian", "Massara", "Njord'volk", "La'vanti",
                            "Seileshi", "Tong'Fei", "Sorama", "Gwai'Non",
                            "Ar'Ruug", "Or'Gog", "Mo'Log'Ath", "Rong'Nol",
                            "Vanasa", "Immakandi", "T'laxine", "Yong'Yao"};

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
}