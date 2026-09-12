package dansplugins.spawnsystem.listeners;

import dansplugins.spawnsystem.data.PersistentData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PlayerRespawnListenerTest {

    private final PersistentData persistentData = mock(PersistentData.class);
    private final Plugin plugin = mock(Plugin.class);
    private final Server server = mock(Server.class);
    private final BukkitScheduler scheduler = mock(BukkitScheduler.class);
    private final PlayerRespawnListener playerRespawnListener =
            new PlayerRespawnListener(persistentData, plugin);

    @BeforeEach
    void wireSchedulerToPlugin() {
        when(plugin.getServer()).thenReturn(server);
        when(server.getScheduler()).thenReturn(scheduler);
    }

    @Test
    void handle_playerWithABedSpawn_isLeftToVanillaRespawn() {
        Player player = mock(Player.class);
        when(player.getBedSpawnLocation()).thenReturn(someLocation());

        playerRespawnListener.handle(respawnOf(player));

        verifyNoInteractions(persistentData);
        verifyNoInteractions(scheduler);
        verify(player, never()).teleport(any(Location.class));
    }

    // A bed spawn wins over a stored custom spawn.
    @Test
    void handle_playerWithBothABedSpawnAndACustomSpawn_prefersTheBedSpawn() {
        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.getBedSpawnLocation()).thenReturn(someLocation());
        when(player.getUniqueId()).thenReturn(playerId);
        HashMap<UUID, Location> spawns = new HashMap<UUID, Location>();
        spawns.put(playerId, someLocation());
        when(persistentData.getPlayerSpawns()).thenReturn(spawns);

        playerRespawnListener.handle(respawnOf(player));

        verifyNoInteractions(scheduler);
        verify(player, never()).teleport(any(Location.class));
    }

    @Test
    void handle_playerWithoutACustomSpawn_isLeftToVanillaRespawn() {
        Player player = mock(Player.class);
        when(player.getBedSpawnLocation()).thenReturn(null);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(persistentData.getPlayerSpawns()).thenReturn(new HashMap<UUID, Location>());

        playerRespawnListener.handle(respawnOf(player));

        verifyNoInteractions(scheduler);
        verify(player, never()).teleport(any(Location.class));
    }

    // The teleport is deferred by one tick so it runs after the vanilla respawn has placed the player;
    // teleporting synchronously inside the event would be overridden by the respawn location.
    @Test
    void handle_playerWithACustomSpawn_schedulesTheTeleportOneTickLaterOnThePlugin() {
        Player player = playerWithCustomSpawn(someLocation());

        playerRespawnListener.handle(respawnOf(player));

        verify(scheduler).runTaskLater(eq(plugin), any(Runnable.class), eq(1L));
        verify(player, never()).teleport(any(Location.class));
        verify(player, never()).sendMessage(anyString());
    }

    @Test
    void handle_playerWithACustomSpawn_teleportsThereAndTellsThePlayerWhenTheTaskRuns() {
        Location customSpawn = new Location(mock(World.class), 12, 70, -34);
        Player player = playerWithCustomSpawn(customSpawn);

        playerRespawnListener.handle(respawnOf(player));
        scheduledTask().run();

        verify(player).teleport(customSpawn);
        verify(player).sendMessage(ChatColor.GREEN + "Teleporting to custom spawn!");
    }

    private Player playerWithCustomSpawn(Location customSpawn) {
        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.getBedSpawnLocation()).thenReturn(null);
        when(player.getUniqueId()).thenReturn(playerId);
        HashMap<UUID, Location> spawns = new HashMap<UUID, Location>();
        spawns.put(playerId, customSpawn);
        when(persistentData.getPlayerSpawns()).thenReturn(spawns);
        return player;
    }

    private Runnable scheduledTask() {
        ArgumentCaptor<Runnable> task = ArgumentCaptor.forClass(Runnable.class);
        verify(scheduler).runTaskLater(eq(plugin), task.capture(), anyLong());
        return task.getValue();
    }

    private PlayerRespawnEvent respawnOf(Player player) {
        return new PlayerRespawnEvent(player, someLocation(), false);
    }

    private Location someLocation() {
        return new Location(mock(World.class), 0, 64, 0);
    }
}
