package dansplugins.spawnsystem.data;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersistentDataTest {

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
}
