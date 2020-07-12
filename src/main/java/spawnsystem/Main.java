package spawnsystem;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.HashMap;

public final class Main extends JavaPlugin implements Listener {

    // saved
    HashMap<String, Location> playerSpawns = new HashMap<>();

    // temporary
    String worldname = "betatwo";
    String[] subcultures = {"Ostendian", "Massara", "Njord'volk", "La'vanti",
                            "Seileshi", "Tong'Fei", "Sorama", "Gwai'Non",
                            "Ar'Ruug", "Or'Gog", "Mo'Log'Ath", "Rong'Nol",
                            "Vanasa", "Immakandi", "T'laxine", "Yong'Yao"};

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        load();
    }

    @Override
    public void onDisable() {
        save();
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
                player.sendMessage(ChatColor.GREEN + "What race would you like to be?\nChoices: \n* Human\n* Greyfolk\n* Horndall\n* Felkata.");
                player.sendMessage(ChatColor.GREEN + "Type /race (race selection) to continue.");
            }
        }

        if (label.equalsIgnoreCase("race")) {
            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (args.length > 0) {

                    String raceSelection = args[0];

                    if (raceSelection.equalsIgnoreCase("human")) {
                        player.sendMessage(ChatColor.GREEN + "What subculture of Human are you?\nChoices: \n* Ostendian\n* Massara\n* Njord'volk\n* La'Vanti.");
                        player.sendMessage(ChatColor.GREEN + "Type /subculture (subculture selection) to continue.");

                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("greyfolk")) {
                        player.sendMessage(ChatColor.GREEN + "What subculture of Greyfolk are you?\nChoices: \n* Seleishi\n* Tong'Fei\n* Sorama\n* Gwai'Non.");
                        player.sendMessage(ChatColor.GREEN + "Type /subculture (subculture selection) to continue.");
                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("horndall")) {
                        player.sendMessage(ChatColor.GREEN + "What subculture of Horndall are you?\nChoices: \n* Ar'Ruug\n* Or'Gog\n* Mo'Log'Ath\n* Rong'Nol.");
                        player.sendMessage(ChatColor.GREEN + "Type /subculture (subculture selection) to continue.");
                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("felkata")) {
                        player.sendMessage(ChatColor.GREEN + "What subculture of Felkata are you?\nChoices: \n* Vanasa\n* Immakandi\n* T'laxine\n* Yong'Yao");
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

                if (args.length > 0) {

                    String selection = args[0];

                    int x, y, z;

                    // set spawn
                    if (selection.equalsIgnoreCase(subcultures[0])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[1])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[2])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[3])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[4])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[5])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[6])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[7])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[8])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[9])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[10])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[11])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[12])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[13])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[14])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[15])) {
                        x = 0;
                        y = 100;
                        z = 0;
                        setPlayersSpawn(player, x, y, z);
                        return true;
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

        player.sendMessage(ChatColor.GREEN + "Spawn set!");

    }

    private void save() {
        saveSpawnFilenames();
        saveSpawns();
    }

    private void saveSpawnFilenames() {

    }

    private void saveSpawns() {

    }

    private void load () {
        loadSpawns();
    }

    private void loadSpawns() {

    }

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent event) {
        if (playerSpawns.containsKey(event.getPlayer().getName())) {
            int seconds = 1;
            getServer().getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().teleport(playerSpawns.get(event.getPlayer().getName()));

                }
            }, seconds * 20);
        }
    }
}
