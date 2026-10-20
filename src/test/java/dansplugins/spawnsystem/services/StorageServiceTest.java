package dansplugins.spawnsystem.services;

import dansplugins.spawnsystem.data.PersistentData;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StorageServiceTest {

    private final Plugin plugin = mock(Plugin.class);
    private final Logger logger = mock(Logger.class);

    // PersistentData is not reached by the parsing step under test, which is why null stands in for it.
    private final StorageService storageService = new StorageService(plugin, null);

    @BeforeEach
    void wireLoggerToPlugin() {
        when(plugin.getLogger()).thenReturn(logger);
    }

    @Test
    void parseSpawnLocation_zeroCoordinates_isKept() {
        World world = mock(World.class);

        Location location = storageService.parseSpawnLocation(world, "0.0", "64.0", "0.0");

        assertNotNull(location);
        assertSame(world, location.getWorld());
        assertEquals(0.0, location.getX());
        assertEquals(64.0, location.getY());
        assertEquals(0.0, location.getZ());
        verify(logger, never()).warning(anyString());
    }

    @Test
    void parseSpawnLocation_nonZeroCoordinates_isKept() {
        World world = mock(World.class);

        Location location = storageService.parseSpawnLocation(world, "100.5", "64.0", "-200.25");

        assertNotNull(location);
        assertEquals(100.5, location.getX());
        assertEquals(64.0, location.getY());
        assertEquals(-200.25, location.getZ());
    }

    @Test
    void parseSpawnLocation_missingCoordinateLine_isDiscarded() {
        assertNull(storageService.parseSpawnLocation(mock(World.class), "100.5", null, "-200.25"));
    }

    @Test
    void parseSpawnLocation_missingCoordinateLine_warnsThroughThePluginLogger() {
        storageService.parseSpawnLocation(mock(World.class), "100.5", null, "-200.25");

        verify(logger).warning("Y position not found in file!");
        verify(logger).warning("One of the variables the spawn location depends on wasn't loaded!");
    }

    @Test
    void parseSpawnLocation_unreadableCoordinateLine_isDiscarded() {
        assertNull(storageService.parseSpawnLocation(mock(World.class), "100.5", "not-a-number", "-200.25"));
    }

    @Test
    void parseSpawnLocation_unreadableCoordinateLine_warnsThroughThePluginLoggerWithTheValue() {
        storageService.parseSpawnLocation(mock(World.class), "100.5", "not-a-number", "-200.25");

        verify(logger).warning("Y position in file couldn't be read as a number: not-a-number");
    }

    @Test
    void parseSpawnLocation_missingWorld_isDiscarded() {
        assertNull(storageService.parseSpawnLocation(null, "100.5", "64.0", "-200.25"));
    }

    @Test
    void parseSpawnLocation_missingWorld_warnsThroughThePluginLogger() {
        storageService.parseSpawnLocation(null, "100.5", "64.0", "-200.25");

        verify(logger).warning("One of the variables the spawn location depends on wasn't loaded!");
    }

    @Test
    void load_noSpawnFilenamesFileYet_logsInfoAndNoWarning(@TempDir Path storageDirectory) {
        StorageService firstStart = new StorageService(plugin, null, storageDirectory + File.separator);

        firstStart.load();

        verify(logger).info("No saved spawns found yet.");
        verify(logger, never()).warning(anyString());
        verify(logger, never()).log(eq(Level.WARNING), anyString(), any(Throwable.class));
    }

    @Test
    void load_emptySpawnFilenamesFile_logsNothing(@TempDir Path storageDirectory) throws IOException {
        Files.createFile(storageDirectory.resolve("spawn-filenames.txt"));
        StorageService noSpawns = new StorageService(plugin, null, storageDirectory + File.separator);

        noSpawns.load();

        verify(logger, never()).info(anyString());
        verify(logger, never()).warning(anyString());
        verify(logger, never()).log(eq(Level.WARNING), anyString(), any(Throwable.class));
    }

    @Test
    void load_unreadableSpawnFilenamesFile_warnsWithTheCause(@TempDir Path storageDirectory) throws IOException {
        // a directory in place of the file exists but cannot be opened for reading
        Files.createDirectory(storageDirectory.resolve("spawn-filenames.txt"));
        StorageService unreadable = new StorageService(plugin, null, storageDirectory + File.separator);

        unreadable.load();

        verify(logger).log(eq(Level.WARNING), eq("Error loading the spawns!"), any(FileNotFoundException.class));
        verify(logger, never()).info(anyString());
    }

    @Test
    void save_playerWithSpawn_writesFilenameListAndRecordInDocumentedFormat(@TempDir Path storageDirectory) throws IOException {
        UUID playerId = UUID.fromString("11111111-2222-3333-4444-555555555555");
        PersistentData persistentData = new PersistentData();
        addSpawn(persistentData, playerId, worldNamed("world_nether"), 100.5, 64.0, -200.25);

        new StorageService(plugin, persistentData, storageDirectory + File.separator).save();

        assertEquals(Collections.singletonList(playerId + ".txt"), readLines(storageDirectory.resolve("spawn-filenames.txt")));
        assertEquals(Arrays.asList(playerId.toString(), "world_nether", "100.5", "64.0", "-200.25"),
                readLines(storageDirectory.resolve(playerId + ".txt")));
        verify(logger, never()).log(eq(Level.WARNING), anyString(), any(Throwable.class));
    }

    @Test
    void save_noSpawns_writesAnEmptyFilenameList(@TempDir Path storageDirectory) throws IOException {
        new StorageService(plugin, new PersistentData(), storageDirectory + File.separator).save();

        assertEquals(Collections.emptyList(), readLines(storageDirectory.resolve("spawn-filenames.txt")));
    }

    @Test
    void save_storageFolderMissing_createsIt(@TempDir Path parentDirectory) {
        Path storageDirectory = parentDirectory.resolve("Kingdom-Spawn-System");

        new StorageService(plugin, new PersistentData(), storageDirectory + File.separator).save();

        assertTrue(Files.isRegularFile(storageDirectory.resolve("spawn-filenames.txt")));
    }

    @Test
    void save_resetPlayer_isLeftOutOfTheFilenameList(@TempDir Path storageDirectory) throws IOException {
        UUID keptId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID resetId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        PersistentData persistentData = new PersistentData();
        addSpawn(persistentData, keptId, worldNamed("world"), 1, 2, 3);
        addSpawn(persistentData, resetId, worldNamed("world"), 4, 5, 6);
        persistentData.resetSpawn(resetId);

        new StorageService(plugin, persistentData, storageDirectory + File.separator).save();

        assertEquals(Collections.singletonList(keptId + ".txt"), readLines(storageDirectory.resolve("spawn-filenames.txt")));
    }

    @Test
    void saveThenLoad_spawnSurvivesTheRoundTrip(@TempDir Path storageDirectory) {
        UUID playerId = UUID.randomUUID();
        PersistentData saved = new PersistentData();
        addSpawn(saved, playerId, worldNamed("world_nether"), 100.5, 64.0, -200.25);
        new StorageService(plugin, saved, storageDirectory + File.separator).save();
        World loadedWorld = worldNamed("world_nether");
        ArgumentCaptor<WorldCreator> requestedWorld = serverReturning(loadedWorld);
        PersistentData loaded = new PersistentData();

        new StorageService(plugin, loaded, storageDirectory + File.separator).load();

        assertEquals("world_nether", requestedWorld.getValue().name());
        assertEquals(Collections.singletonList(playerId), loaded.getPlayersWithSpawns());
        Location spawn = loaded.getPlayerSpawns().get(playerId);
        assertSame(loadedWorld, spawn.getWorld());
        assertEquals(100.5, spawn.getX());
        assertEquals(64.0, spawn.getY());
        assertEquals(-200.25, spawn.getZ());
        verify(logger, never()).warning(anyString());
    }

    @Test
    void load_listedRecordMissingFromDisk_warnsAndStillLoadsTheNextRecord(@TempDir Path storageDirectory) throws IOException {
        UUID missingId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        UUID presentId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        writeLines(storageDirectory.resolve("spawn-filenames.txt"), missingId + ".txt", presentId + ".txt");
        writeLines(storageDirectory.resolve(presentId + ".txt"), presentId.toString(), "world", "1.0", "2.0", "3.0");
        serverReturning(worldNamed("world"));
        PersistentData loaded = new PersistentData();

        new StorageService(plugin, loaded, storageDirectory + File.separator).load();

        verify(logger).log(eq(Level.WARNING), eq("An error occurred loading the file " + missingId + ".txt."),
                any(FileNotFoundException.class));
        assertEquals(Collections.singletonList(presentId), loaded.getPlayersWithSpawns());
    }

    @Test
    void load_recordWithOnlyAUUID_warnsAndIsSkipped(@TempDir Path storageDirectory) throws IOException {
        UUID playerId = UUID.randomUUID();
        writeLines(storageDirectory.resolve("spawn-filenames.txt"), playerId + ".txt");
        writeLines(storageDirectory.resolve(playerId + ".txt"), playerId.toString());
        Server server = mock(Server.class);
        when(plugin.getServer()).thenReturn(server);
        PersistentData loaded = new PersistentData();

        new StorageService(plugin, loaded, storageDirectory + File.separator).load();

        verify(logger).warning("World name not found in file!");
        verify(server, never()).createWorld(any(WorldCreator.class));
        assertTrue(loaded.getPlayersWithSpawns().isEmpty());
        assertTrue(loaded.getPlayerSpawns().isEmpty());
    }

    @Test
    void load_recordWithAnUnreadableCoordinate_isSkipped(@TempDir Path storageDirectory) throws IOException {
        UUID playerId = UUID.randomUUID();
        writeLines(storageDirectory.resolve("spawn-filenames.txt"), playerId + ".txt");
        writeLines(storageDirectory.resolve(playerId + ".txt"), playerId.toString(), "world", "1.0", "high", "3.0");
        serverReturning(worldNamed("world"));
        PersistentData loaded = new PersistentData();

        new StorageService(plugin, loaded, storageDirectory + File.separator).load();

        verify(logger).warning("Y position in file couldn't be read as a number: high");
        assertFalse(loaded.getPlayerSpawns().containsKey(playerId));
        assertTrue(loaded.getPlayersWithSpawns().isEmpty());
    }

    @Test
    void load_recordWhoseFirstLineIsNotAUUID_throwsOutOfLoadAndSkipsLaterRecords(@TempDir Path storageDirectory) throws IOException {
        // Characterizes current behaviour: the UUID parse sits outside the per-record catch, so one
        // corrupt record aborts the whole load (and, at startup, onEnable) instead of being skipped (#121).
        UUID laterId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        writeLines(storageDirectory.resolve("spawn-filenames.txt"), "corrupt.txt", laterId + ".txt");
        writeLines(storageDirectory.resolve("corrupt.txt"), "not-a-uuid", "world", "1.0", "2.0", "3.0");
        writeLines(storageDirectory.resolve(laterId + ".txt"), laterId.toString(), "world", "1.0", "2.0", "3.0");
        serverReturning(worldNamed("world"));
        PersistentData loaded = new PersistentData();
        StorageService storageService = new StorageService(plugin, loaded, storageDirectory + File.separator);

        assertThrows(IllegalArgumentException.class, storageService::load);
        assertTrue(loaded.getPlayersWithSpawns().isEmpty());
    }

    private static void addSpawn(PersistentData persistentData, UUID playerId, World world, double x, double y, double z) {
        persistentData.getPlayerSpawns().put(playerId, new Location(world, x, y, z));
        persistentData.getPlayersWithSpawns().add(playerId);
    }

    private static World worldNamed(String name) {
        World world = mock(World.class);
        when(world.getName()).thenReturn(name);
        return world;
    }

    private ArgumentCaptor<WorldCreator> serverReturning(World world) {
        Server server = mock(Server.class);
        ArgumentCaptor<WorldCreator> requestedWorld = ArgumentCaptor.forClass(WorldCreator.class);
        when(plugin.getServer()).thenReturn(server);
        when(server.createWorld(requestedWorld.capture())).thenReturn(world);
        return requestedWorld;
    }

    private static void writeLines(Path file, String... lines) throws IOException {
        Files.write(file, Arrays.asList(lines), StandardCharsets.UTF_8);
    }

    private static List<String> readLines(Path file) throws IOException {
        return Files.readAllLines(file, StandardCharsets.UTF_8);
    }
}
