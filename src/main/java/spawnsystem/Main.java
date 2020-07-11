package spawnsystem;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin {

    // saved
    HashMap<String, Location> playerSpawns = new HashMap<>();

    // temporary
    String worldname = "continent";
    String[] subcultures = {"Ostendian", "Massara", "Njord'volk", "La'vanti",
                            "Seileshi", "Tong'Fei", "Sorama", "Gwai'Non",
                            "Ar'Ruug", "Or'Gog", "Mo'Log'Ath", "Rong'Nol",
                            "Vanasa", "Immakandi", "T'laxine", "Yong'Yao"};

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (label.equalsIgnoreCase("start")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                // if hasn't already chosen subculture.
                if (playerSpawns.containsKey(player.getName())) {
                    player.sendMessage(ChatColor.RED + "You already went through the start process!");
                    return false;
                }

                player.sendMessage(ChatColor.GREEN + "Before you can start playing, we need to know where to set your spawn!");
                player.sendMessage(ChatColor.GREEN + "What race would you like to be? There are Humans, Greyfolk, Horndall and Felkata.");
                player.sendMessage(ChatColor.GREEN + "Type /race (race selection) to continue.");
            }
        }

        if (label.equalsIgnoreCase("race")) {
            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (args.length > 0) {

                    String raceSelection = args[0];

                    if (raceSelection.equalsIgnoreCase("human")) {
                        player.sendMessage(ChatColor.GREEN + "What subculture of Human are you? There is Ostendian, Massara, Njord'volk and La'Vanti.");
                        player.sendMessage(ChatColor.GREEN + "Type /subculture (subculture selection) to continue.");

                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("greyfolk")) {
                        player.sendMessage(ChatColor.GREEN + "What subculture of Human are you? There is Seleishi, Tong'Fei, Sorama and Gwai'Non.");
                        player.sendMessage(ChatColor.GREEN + "Type /subculture (subculture selection) to continue.");
                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("horndall")) {
                        player.sendMessage(ChatColor.GREEN + "What subculture of Human are you? There is Ar'Ruug, Or'Gog, Mo'Log'Ath and Rong'Nol.");
                        player.sendMessage(ChatColor.GREEN + "Type /subculture (subculture selection) to continue.");
                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("felkata")) {
                        player.sendMessage(ChatColor.GREEN + "What subculture of Human are you? There is Vanasa, Immakandi, T'laxine, and Yong'Yao");
                        player.sendMessage(ChatColor.GREEN + "Type /subculture (subculture selection) to continue.");
                        return true;
                    }

                    // not a valid race
                    player.sendMessage(ChatColor.RED + "That isn't a valid race!");

                }
                else {
                    player.sendMessage(ChatColor.RED + "Usage: /race (race selection)");
                    player.sendMessage(ChatColor.RED + "The races are Human, Greyfolk, Horndall and Felkata.");
                }

            }
        }

        if (label.equalsIgnoreCase("subculture")) {
            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (args.length > 1) {

                    String selection = args[0];

                    // set spawn
                    if (selection.equalsIgnoreCase(subcultures[0])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[1])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[2])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[3])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[4])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[5])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[6])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[7])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[8])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[9])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[10])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[11])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[12])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[13])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[14])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }
                    if (selection.equalsIgnoreCase(subcultures[15])) {
                        setPlayersSpawn(player,0, 0, 0);
                    }

                    // not a valid subculture
                    player.sendMessage(ChatColor.RED + "That isn't a valid subculture! Type /subcultures to view all subcultures.");

                }

            }
        }

        if (label.equalsIgnoreCase("subcultures")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                player.sendMessage(ChatColor.AQUA + "Humans: " + subcultures[0] + ", " + subcultures[1] + ", " + subcultures[2] + ", " + subcultures[3]);
                player.sendMessage(ChatColor.AQUA + "Greyfolk: " + subcultures[4] + ", " + subcultures[5] + ", " + subcultures[6] + ", " + subcultures[7]);
                player.sendMessage(ChatColor.AQUA + "Horndall: " + subcultures[8] + ", " + subcultures[9] + ", " + subcultures[10] + ", " + subcultures[11]);
                player.sendMessage(ChatColor.AQUA + "Felkata: " + subcultures[12] + ", " + subcultures[13] + ", " + subcultures[14] + ", " + subcultures[15]);

            }

        }

        return false;
    }

    private void setPlayersSpawn(Player player, int x, int y, int z) {

        Location spawnLocation = new Location(getServer().getWorld(worldname), x, y, z);

        // set spawn
        playerSpawns.put(player.getName(), spawnLocation);

        // teleport player
        player.teleport(spawnLocation);

    }
}
