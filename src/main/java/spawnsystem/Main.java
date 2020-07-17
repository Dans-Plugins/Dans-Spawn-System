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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public final class Main extends JavaPlugin implements Listener {

    // saved
    HashMap<String, Location> playerSpawns = new HashMap<>();
    ArrayList<String> playersWithSpawns = new ArrayList<>();

    // temporary
    String worldname = "world";
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
                        x = -3394;
                        y = 66;
                        z = 61;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[1])) {
                        x = -2785;
                        y = 85;
                        z = 1219;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[2])) {
                        x = -1966;
                        y = 142;
                        z = -1841;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[3])) {
                        x = 473;
                        y = 74;
                        z = 437;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[4])) {
                        x = -136;
                        y = 87;
                        z = 138;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[5])) {
                        x = -599;
                        y = 105;
                        z = -868;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[6])) {
                        x = -1086;
                        y = 115;
                        z = 494;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[7])) {
                        x = 685;
                        y = 81;
                        z = -755;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[8])) {
                        x = 3728;
                        y = 100;
                        z = -1232;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[9])) { // or gog
                        x = 2264;
                        y = 90;
                        z = -1043;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[10])) { // mo
                        x = 4970;
                        y = 91;
                        z = -1702;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[11])) { // rong
                        x = 1265;
                        y = 86;
                        z = -1869;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[12])) {
                        x = -777;
                        y = 73;
                        z = 1872;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[13])) {
                        x = 961;
                        y = 85;
                        z = 1816;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[14])) {
                        x = -2843;
                        y = 77;
                        z = 2002;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[15])) {
                        x = 2222;
                        y = 84;
                        z = 1788;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
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
        if (!playerSpawns.containsKey(player.getName())) {
            playerSpawns.put(player.getName(), spawnLocation);
            playersWithSpawns.add(player.getName());
        }
        else {
            player.sendMessage(ChatColor.RED + "You have already set your spawn! If you're starting a new character please see an admin for assistance.");
            return;
        }


        // teleport player
        player.teleport(spawnLocation);

        player.sendMessage(ChatColor.GREEN + "Spawn set!");

    }

    private void teleportIfOverriding(String[] args, Player player, int x, int y, int z) {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("override")) {
                if (player.hasPermission("spawnsystem.override")) {
                    Location teleportLocation = new Location(getServer().getWorld(worldname), x, y, z);
                    player.teleport(teleportLocation);
                }
                else {
                    player.sendMessage(ChatColor.RED + "Sorry! In order to use the override, you need the following permission: 'spawnsystem.override'");
                }
            }
        }
    }

    private void save() {
        saveSpawnFilenames();
        saveSpawns();
    }

    private void saveSpawnFilenames() {
        try {
            File saveFolder = new File("./plugins/Kingdom-Spawn-System/");
            if (!saveFolder.exists()) {
                saveFolder.mkdir();
            }
            File saveFile = new File("./plugins/Kingdom-Spawn-System/" + "spawn-filenames.txt");
            if (saveFile.createNewFile()) {
                System.out.println("Save file for spawn filenames created.");
            } else {
                System.out.println("Save file for spawn filenames already exists. Overwriting.");
            }

            FileWriter saveWriter = new FileWriter(saveFile);

            // actual saving takes place here
            for (int i = 0; i < playersWithSpawns.size(); i++) {
                saveWriter.write(playersWithSpawns.get(i));
            }

            saveWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred while saving spawn filenames.");
        }
    }

    private void saveSpawns() {
        for (String playerName : playersWithSpawns) {

            try {
                File saveFolder = new File("./plugins/Kingdom-Spawn-System/");
                if (!saveFolder.exists()) {
                    saveFolder.mkdir();
                }
                File saveFile = new File("./plugins/Kingdom-Spawn-System/" + playerName + ".txt");
                if (saveFile.createNewFile()) {
                    System.out.println("Save file for record of " + playerName + " created.");
                } else {
                    System.out.println("Save file for record of " + playerName + " already exists. Altering.");
                }

                FileWriter saveWriter = new FileWriter("./plugins/Kingdom-Spawn-System/" + playerName + ".txt");

                // actual saving takes place here
                saveWriter.write(playerName + "\n");

                // save details
                saveWriter.write(playerSpawns.get(playerName).getWorld().getName() + "\n");
                saveWriter.write(playerSpawns.get(playerName).getX() + "\n");
                saveWriter.write(playerSpawns.get(playerName).getY() + "\n");
                saveWriter.write(playerSpawns.get(playerName).getZ() + "\n");

                saveWriter.close();

                System.out.println("Successfully saved record belonging to  " + playerName + ".");

            } catch (IOException e) {
                System.out.println("An error occurred saving the record belonging to " + playerName);
                e.printStackTrace();
            }

        }
    }

    private void load () {
        loadSpawns();
    }

    private void loadSpawns() {
        try {
            System.out.println("Attempting to load spawns...");
            File loadFile = new File("./plugins/Kingdom-Spawn-System/" + "spawn-filenames.txt");
            Scanner loadReader = new Scanner(loadFile);

            // actual loading
            while (loadReader.hasNextLine()) {
                String filename = loadReader.nextLine() + ".txt";

                // load
                try {
                    File loadFile2 = new File("./plugins/Kingdom-Spawn-System/" + filename);
                    Scanner loadReader2 = new Scanner(loadFile2);

                    String playerName = "";

                    // actual loading
                    if (loadReader2.hasNextLine()) {
                        playerName = loadReader2.nextLine();
                    }

                    World world = null;
                    double x = 0;
                    double y = 0;
                    double z = 0;

                    try {
                        System.out.println("Attempting to load spawn location for " + playerName + "...");

                        if (loadReader2.hasNextLine()) {
                            world = getServer().createWorld(new WorldCreator(loadReader2.nextLine()));
                            System.out.println("World successfully acquired.");
                        }
                        else {
                            System.out.println("World name not found in file!");
                        }
                        if (loadReader2.hasNextLine()) {
                            x = Double.parseDouble(loadReader2.nextLine());
                        }
                        else {
                            System.out.println("X position not found in file!");
                        }
                        if (loadReader2.hasNextLine()) {//
                            y = Double.parseDouble(loadReader2.nextLine());
                        }
                        else {
                            System.out.println("Y position not found in file!");
                        }
                        if (loadReader2.hasNextLine()) {
                            z = Double.parseDouble(loadReader2.nextLine());
                        }
                        else {
                            System.out.println("Z position not found in file!");
                        }

                        // set location
                        if (world != null && x != 0 && y != 0 && z != 0) {
                            playerSpawns.put(playerName, new Location(world, x, y, z));
                            playersWithSpawns.add(playerName);
                            System.out.println("Spawn of " + playerName + " successfully set to " + x + ", " + y + ", " + z + ".");
                        }
                        else {
                            System.out.println("One of the variables the spawn location depends on wasn't loaded!");
                        }

                    }
                    catch(Exception e) {
                        System.out.println("An error occurred loading the spawn position.");
                        e.printStackTrace();
                    }

                    loadReader2.close();
                    System.out.println("Spawn of " + playerName + " successfully loaded.");
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred loading the file " + filename + ".");
                    e.printStackTrace();
                }

            }

            loadReader.close();
            System.out.println("Spawns successfully loaded.");
        } catch (FileNotFoundException e) {
            System.out.println("Error loading the spawns!");
        }
    }

    @EventHandler()
    public void onDeath(PlayerDeathEvent event) {
        event.setKeepLevel(true);
    }


    @EventHandler()
    public void onRespawn(PlayerRespawnEvent event) {
        if (playerSpawns.containsKey(event.getPlayer().getName())) {
            getServer().getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().teleport(playerSpawns.get(event.getPlayer().getName()));
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Teleporting to custom spawn!");
                }
            }, 1);
        }
    }
}
