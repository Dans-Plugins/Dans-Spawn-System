package dansplugins.spawnsystem.services;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class StorageServiceTest {

    // DansSpawnSystem is final and so cannot be mocked; neither collaborator is reached by the parsing
    // step under test, which is why null stands in for both.
    private final StorageService storageService = new StorageService(null, null);

    @Test
    void parseSpawnLocation_zeroCoordinates_isKept() {
        World world = mock(World.class);

        Location location = storageService.parseSpawnLocation(world, "0.0", "64.0", "0.0");

        assertNotNull(location);
        assertSame(world, location.getWorld());
        assertEquals(0.0, location.getX());
        assertEquals(64.0, location.getY());
        assertEquals(0.0, location.getZ());
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
    void parseSpawnLocation_unreadableCoordinateLine_isDiscarded() {
        assertNull(storageService.parseSpawnLocation(mock(World.class), "100.5", "not-a-number", "-200.25"));
    }

    @Test
    void parseSpawnLocation_missingWorld_isDiscarded() {
        assertNull(storageService.parseSpawnLocation(null, "100.5", "64.0", "-200.25"));
    }
}
