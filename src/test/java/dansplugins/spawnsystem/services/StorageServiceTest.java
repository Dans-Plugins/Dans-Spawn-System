package dansplugins.spawnsystem.services;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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
}
