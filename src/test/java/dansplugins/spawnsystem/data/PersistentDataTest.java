package dansplugins.spawnsystem.data;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersistentDataTest {

    @Test
    void setPlayersSpawn_firstSelection_storesAndTracksTheSpawnOnce() {
        PersistentData persistentData = new PersistentData();
        UUID playerId = UUID.randomUUID();
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(playerId);
        World world = mock(World.class);

        persistentData.setPlayersSpawn(player, world, 100, 64, -200);

        Location storedSpawn = persistentData.getPlayerSpawns().get(playerId);
        assertSame(world, storedSpawn.getWorld());
        assertEquals(100.0, storedSpawn.getX());
        assertEquals(64.0, storedSpawn.getY());
        assertEquals(-200.0, storedSpawn.getZ());
        assertEquals(Collections.singletonList(playerId), persistentData.getPlayersWithSpawns());
    }

    @Test
    void setPlayersSpawn_firstSelection_teleportsToTheBlockCornerFacingSouthAndConfirms() {
        PersistentData persistentData = new PersistentData();
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        World world = mock(World.class);

        persistentData.setPlayersSpawn(player, world, 100, 64, -200);

        // the corner of the named block, not its centre, and yaw 0 (south), as USER_GUIDE.md describes
        ArgumentCaptor<Location> destination = ArgumentCaptor.forClass(Location.class);
        verify(player).teleport(destination.capture());
        assertSame(world, destination.getValue().getWorld());
        assertEquals(100.0, destination.getValue().getX());
        assertEquals(64.0, destination.getValue().getY());
        assertEquals(-200.0, destination.getValue().getZ());
        assertEquals(0.0f, destination.getValue().getYaw());
        assertEquals(0.0f, destination.getValue().getPitch());
        verify(player).sendMessage(ChatColor.GREEN + "Spawn set!");
    }

    @Test
    void setPlayersSpawn_secondCall_refusesWithoutTeleportingOrTrackingThePlayerTwice() {
        PersistentData persistentData = new PersistentData();
        UUID playerId = UUID.randomUUID();
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(playerId);
        World world = mock(World.class);
        persistentData.setPlayersSpawn(player, world, 1, 2, 3);

        persistentData.setPlayersSpawn(player, world, 100, 200, 300);

        verify(player, times(1)).teleport(any(Location.class));
        verify(player, times(1)).sendMessage(ChatColor.GREEN + "Spawn set!");
        verify(player).sendMessage(ChatColor.RED + "You have already set your spawn! If you're starting a new character please see an admin for assistance.");
        assertEquals(Collections.singletonList(playerId), persistentData.getPlayersWithSpawns());
    }

    @Test
    void setPlayersSpawn_secondCallForSamePlayer_doesNotOverwriteSpawn() {
        PersistentData persistentData = new PersistentData();
        UUID playerId = UUID.randomUUID();
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(playerId);
        when(player.getName()).thenReturn("SomePlayer");
        World world = mock(World.class);

        persistentData.setPlayersSpawn(player, world, 1, 2, 3);
        persistentData.setPlayersSpawn(player, world, 100, 200, 300);

        Location storedSpawn = persistentData.getPlayerSpawns().get(playerId);
        assertEquals(1, storedSpawn.getBlockX());
        assertEquals(2, storedSpawn.getBlockY());
        assertEquals(3, storedSpawn.getBlockZ());
    }

    @Test
    void resetSpawn_playerWithSpawnSet_removesSpawnAndPlayerFromTrackedList() {
        PersistentData persistentData = new PersistentData();
        UUID playerId = UUID.randomUUID();
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(playerId);
        World world = mock(World.class);
        persistentData.setPlayersSpawn(player, world, 1, 2, 3);

        persistentData.resetSpawn(playerId);

        assertNull(persistentData.getPlayerSpawns().get(playerId));
        assertFalse(persistentData.getPlayersWithSpawns().contains(playerId));
    }

    @Test
    void resetSpawn_playerWithoutSpawnSet_doesNotThrow() {
        PersistentData persistentData = new PersistentData();
        UUID playerId = UUID.randomUUID();

        persistentData.resetSpawn(playerId);

        assertTrue(persistentData.getPlayersWithSpawns().isEmpty());
    }
}
