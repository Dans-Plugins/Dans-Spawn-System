package dansplugins.spawnsystem.services;

import dansplugins.spawnsystem.data.PersistentData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class StorageService {
    private final Plugin plugin;
    private final PersistentData persistentData;
    private final String storageDirectory;

    // Cleared only when load() has read the spawn list. Until then save() refuses to run: the
    // in-memory spawns are empty, and writing them would replace spawn-filenames.txt with an
    // empty list, leaving every player's record on disk but never read again. onDisable() calls
    // save(), and Bukkit still runs onDisable() for a plugin whose onEnable() threw before load().
    private boolean loadFailed = true;

    // Entries of spawn-filenames.txt whose record file exists but could not be loaded. They are
    // written back into the list on save, so a record that failed to load once (a hand edit, a
    // world that was not there at startup) is tried again on the next start instead of dropping
    // out of the list for good.
    private final Set<String> unloadedEntries = new LinkedHashSet<>();

    public StorageService(Plugin plugin, PersistentData persistentData) {
        this(plugin, persistentData, "./plugins/Kingdom-Spawn-System/");
    }

    StorageService(Plugin plugin, PersistentData persistentData, String storageDirectory) {
        this.plugin = plugin;
        this.persistentData = persistentData;
        this.storageDirectory = storageDirectory;
    }

    /**
     * Writes the spawn list and every spawn record. Refused, with the files left as they were,
     * until {@link #load()} has read the list.
     */
    public void save() {
        if (loadFailed) {
            plugin.getLogger().severe("Spawns were not saved because spawn-filenames.txt was not read when the plugin "
                    + "was enabled. The files in " + storageDirectory + " have been left as they were.");
            return;
        }
        saveSpawnFilenames();
        saveSpawns();
    }

    /** Whether the spawn list has not been read (load failed or never ran), in which case save() refuses. */
    public boolean isLoadFailed() {
        return loadFailed;
    }

    // Written to a sibling temp file and moved into place, so a server killed or a disk filled
    // mid-save leaves the previous file whole instead of a truncated one. The platform default
    // charset is kept: it is what FileWriter wrote and what Scanner reads.
    private void writeAtomically(File file, String content) throws IOException {
        Path target = file.toPath();
        Path temp = target.resolveSibling(file.getName() + ".tmp");
        try (Writer writer = Files.newBufferedWriter(temp, Charset.defaultCharset())) {
            writer.write(content);
        }
        try {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void saveSpawnFilenames() {
        try {
            File saveFolder = new File(storageDirectory);
            if (!saveFolder.exists()) {
                saveFolder.mkdir();
            }
            File saveFile = new File(storageDirectory + "spawn-filenames.txt");

            Set<String> entries = new LinkedHashSet<>();
            for (int i = 0; i < persistentData.getPlayersWithSpawns().size(); i++) {
                entries.add(persistentData.getPlayersWithSpawns().get(i) + ".txt");
            }
            entries.addAll(unloadedEntries);
            StringBuilder content = new StringBuilder();
            for (String entry : entries) {
                content.append(entry).append("\n");
            }
            writeAtomically(saveFile, content.toString());

        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "An error occurred while saving spawn filenames.", e);
        }
    }

    private void saveSpawns() {
        for (UUID playerName : persistentData.getPlayersWithSpawns()) {
            Location spawnLocation = persistentData.getPlayerSpawns().get(playerName);

            // read before anything is written, so a record whose world is gone keeps its file
            String worldName;
            try {
                worldName = spawnLocation.getWorld().getName();
            } catch (IllegalArgumentException e) {
                // skip only this record, so the ones after it are still saved
                plugin.getLogger().log(Level.WARNING, "The spawn belonging to " + playerName + " wasn't saved, because its world is no longer loaded. Its file was left as it was.", e);
                continue;
            }

            try {
                File saveFolder = new File(storageDirectory);
                if (!saveFolder.exists()) {
                    saveFolder.mkdir();
                }
                File saveFile = new File(storageDirectory + playerName + ".txt");
                writeAtomically(saveFile, playerName.toString() + "\n"
                        + worldName + "\n"
                        + spawnLocation.getX() + "\n"
                        + spawnLocation.getY() + "\n"
                        + spawnLocation.getZ() + "\n");

            } catch (IOException e) {
                plugin.getLogger().log(Level.WARNING, "An error occurred saving the record belonging to " + playerName, e);
            }

        }
    }

    public void load () {
        loadSpawns();
    }

    private void loadSpawns() {
        unloadedEntries.clear();
        try {
            File loadFile = new File(storageDirectory + "spawn-filenames.txt");
            if (!loadFile.exists()) {
                // the list is written only on a clean shutdown, so it is absent until the first one
                plugin.getLogger().info("No saved spawns found yet.");
                loadFailed = false;
                return;
            }
            Scanner loadReader = new Scanner(loadFile);

            // actual loading
            while (loadReader.hasNextLine()) {
                String filename = loadReader.nextLine();

                // load
                try {
                    File loadFile2 = new File(storageDirectory + filename);
                    Scanner loadReader2 = new Scanner(loadFile2);

                    UUID playerUUID = null;

                    // actual loading
                    if (loadReader2.hasNextLine()) {
                        try {
                            playerUUID = UUID.fromString(loadReader2.nextLine());
                        } catch (IllegalArgumentException e) {
                            // skip only this record, so the ones listed after it still load
                            plugin.getLogger().log(Level.WARNING, "The file " + filename + " doesn't start with a player UUID, so it was skipped.", e);
                            loadReader2.close();
                            unloadedEntries.add(filename);
                            continue;
                        }
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
                        if (spawnLocation != null && playerUUID != null) {
                            persistentData.getPlayerSpawns().put(playerUUID, spawnLocation);
                            persistentData.getPlayersWithSpawns().add(playerUUID);
                        }
                        else {
                            unloadedEntries.add(filename);
                        }

                    }
                    catch(Exception e) {
                        plugin.getLogger().log(Level.WARNING, "An error occurred loading the spawn position.", e);
                        unloadedEntries.add(filename);
                    }

                    loadReader2.close();
                } catch (FileNotFoundException e) {
                    plugin.getLogger().log(Level.WARNING, "An error occurred loading the file " + filename + ".", e);
                }

            }

            loadReader.close();
            loadFailed = false;
        } catch (FileNotFoundException e) {
            loadFailed = true;
            plugin.getLogger().log(Level.WARNING, "Error loading the spawns!", e);
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
