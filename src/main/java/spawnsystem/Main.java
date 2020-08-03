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

    // subsystems
    CommandSubsystem commands = new CommandSubsystem(this);
    UtilitySubsystem utilities = new UtilitySubsystem(this);

    // saved
    HashMap<String, Location> playerSpawns = new HashMap<>();
    ArrayList<String> playersWithSpawns = new ArrayList<>();

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
        return commands.interpretCommand(sender, label, args);
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
                saveWriter.write(playersWithSpawns.get(i) + ".txt" + "\n");
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
                String filename = loadReader.nextLine();

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
