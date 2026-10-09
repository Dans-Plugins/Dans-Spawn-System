package dansplugins.spawnsystem.services;

import dansplugins.spawnsystem.data.PersistentData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;

public class StorageService {
    private final Plugin plugin;
    private final PersistentData persistentData;

    public StorageService(Plugin plugin, PersistentData persistentData) {
        this.plugin = plugin;
        this.persistentData = persistentData;
    }

    public void save() {
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
            saveFile.createNewFile();

            FileWriter saveWriter = new FileWriter(saveFile);

            // actual saving takes place here
            for (int i = 0; i < persistentData.getPlayersWithSpawns().size(); i++) {
                saveWriter.write(persistentData.getPlayersWithSpawns().get(i) + ".txt" + "\n");
            }

            saveWriter.close();

        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "An error occurred while saving spawn filenames.", e);
        }
    }

    private void saveSpawns() {
        for (UUID playerName : persistentData.getPlayersWithSpawns()) {

            try {
                File saveFolder = new File("./plugins/Kingdom-Spawn-System/");
                if (!saveFolder.exists()) {
                    saveFolder.mkdir();
                }
                File saveFile = new File("./plugins/Kingdom-Spawn-System/" + playerName + ".txt");
                saveFile.createNewFile();

                FileWriter saveWriter = new FileWriter("./plugins/Kingdom-Spawn-System/" + playerName + ".txt");

                // actual saving takes place here
                saveWriter.write(playerName.toString() + "\n");

                // save details
                saveWriter.write(persistentData.getPlayerSpawns().get(playerName).getWorld().getName() + "\n");
                saveWriter.write(persistentData.getPlayerSpawns().get(playerName).getX() + "\n");
                saveWriter.write(persistentData.getPlayerSpawns().get(playerName).getY() + "\n");
                saveWriter.write(persistentData.getPlayerSpawns().get(playerName).getZ() + "\n");

                saveWriter.close();

            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING, "An error occurred saving the record belonging to " + playerName, e);
            }

        }
    }

    public void load () {
        loadSpawns();
    }

    private void loadSpawns() {
        try {
            File loadFile = new File("./plugins/Kingdom-Spawn-System/" + "spawn-filenames.txt");
            Scanner loadReader = new Scanner(loadFile);

            // actual loading
            while (loadReader.hasNextLine()) {
                String filename = loadReader.nextLine();

                // load
                try {
                    File loadFile2 = new File("./plugins/Kingdom-Spawn-System/" + filename);
                    Scanner loadReader2 = new Scanner(loadFile2);

                    UUID playerUUID = null;

                    // actual loading
                    if (loadReader2.hasNextLine()) {
                        playerUUID = UUID.fromString(loadReader2.nextLine());
                    }

                    try {
                        String worldName = nextLineOrNull(loadReader2);
                        String xLine = nextLineOrNull(loadReader2);
                        String yLine = nextLineOrNull(loadReader2);
                        String zLine = nextLineOrNull(loadReader2);

                        World world = null;
                        if (worldName != null) {
                            world = plugin.getServer().createWorld(new WorldCreator(worldName));
                        }
                        else {
                            plugin.getLogger().warning("World name not found in file!");
                        }

                        // set location
                        Location spawnLocation = parseSpawnLocation(world, xLine, yLine, zLine);
                        if (spawnLocation != null) {
                            persistentData.getPlayerSpawns().put(playerUUID, spawnLocation);
                            persistentData.getPlayersWithSpawns().add(playerUUID);
                        }

                    }
                    catch(Exception e) {
                        plugin.getLogger().log(Level.WARNING, "An error occurred loading the spawn position.", e);
                    }

                    loadReader2.close();
                } catch (FileNotFoundException e) {
                    plugin.getLogger().log(Level.WARNING, "An error occurred loading the file " + filename + ".", e);
                }

            }

            loadReader.close();
        } catch (FileNotFoundException e) {
            // also reached on a first start, before any spawn has been saved, so no stack trace
            plugin.getLogger().warning("Error loading the spawns!");
        }
    }

    /**
     * Builds a spawn location out of the raw lines of a saved spawn record.
     *
     * A null line means the value was absent from the file. Absence is what makes a record unusable: a
     * coordinate that reads zero is a legitimate position near the world origin and is kept as written.
     *
     * @return the spawn location, or null if the world or any coordinate was missing or unreadable
     */
    Location parseSpawnLocation(World world, String xLine, String yLine, String zLine) {
        Double x = parseCoordinate(xLine, "X");
        Double y = parseCoordinate(yLine, "Y");
        Double z = parseCoordinate(zLine, "Z");

        if (world == null || x == null || y == null || z == null) {
            plugin.getLogger().warning("One of the variables the spawn location depends on wasn't loaded!");
            return null;
        }

        return new Location(world, x, y, z);
    }

    private Double parseCoordinate(String line, String axis) {
        if (line == null) {
            plugin.getLogger().warning(axis + " position not found in file!");
            return null;
        }
        try {
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            plugin.getLogger().warning(axis + " position in file couldn't be read as a number: " + line);
            return null;
        }
    }

    private String nextLineOrNull(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            return null;
        }
        return scanner.nextLine();
    }

}
