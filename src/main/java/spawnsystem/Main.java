package spawnsystem;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import spawnsystem.EventHandlers.PlayerDeathEventHandler;
import spawnsystem.EventHandlers.PlayerRespawnEventHandler;
import spawnsystem.Subsystems.CommandSubsystem;
import spawnsystem.Subsystems.StorageSubsystem;
import spawnsystem.Subsystems.UtilitySubsystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public final class Main extends JavaPlugin implements Listener {

    // subsystems
    public CommandSubsystem commands = new CommandSubsystem(this);
    public UtilitySubsystem utilities = new UtilitySubsystem(this);
    public StorageSubsystem storage = new StorageSubsystem(this);

    // saved
    public HashMap<String, Location> playerSpawns = new HashMap<>();
    public ArrayList<String> playersWithSpawns = new ArrayList<>();

    // temporary
    public String worldname = "KingdomsDarkAges";
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
}
