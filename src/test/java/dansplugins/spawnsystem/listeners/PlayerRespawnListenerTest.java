package dansplugins.spawnsystem.listeners;

import dansplugins.spawnsystem.data.PersistentData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PlayerRespawnListenerTest {

    private final PersistentData persistentData = mock(PersistentData.class);

    // DansSpawnSystem is final and so cannot be mocked. Both branches exercised here return before the
    // plugin reference is dereferenced, which is why null stands in for it; the remaining branch schedules
    // the teleport through the plugin's server and cannot be reached without a mockable collaborator.
    private final PlayerRespawnListener playerRespawnListener =
            new PlayerRespawnListener(persistentData, null);

    @Test
    void handle_playerWithABedSpawn_isLeftToVanillaRespawn() {
        Player player = mock(Player.class);
        when(player.getBedSpawnLocation()).thenReturn(someLocation());

        playerRespawnListener.handle(respawnOf(player));

        verifyNoInteractions(persistentData);
        verify(player, never()).teleport(any(Location.class));
    }

    // A bed spawn wins over a custom spawn: the stored spawn is never consulted.
    @Test
    void handle_playerWithBothABedSpawnAndACustomSpawn_prefersTheBedSpawn() {
        Player player = mock(Player.class);
        when(player.getBedSpawnLocation()).thenReturn(someLocation());
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());

        playerRespawnListener.handle(respawnOf(player));

        verifyNoInteractions(persistentData);
    }

    @Test
    void handle_playerWithoutACustomSpawn_isLeftToVanillaRespawn() {
        Player player = mock(Player.class);
        when(player.getBedSpawnLocation()).thenReturn(null);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(persistentData.getPlayerSpawns()).thenReturn(new HashMap<UUID, Location>());

        playerRespawnListener.handle(respawnOf(player));

        verify(player, never()).teleport(any(Location.class));
    }

    private PlayerRespawnEvent respawnOf(Player player) {
        return new PlayerRespawnEvent(player, someLocation(), false);
    }

    private Location someLocation() {
        return new Location(mock(World.class), 0, 64, 0);
    }
}
