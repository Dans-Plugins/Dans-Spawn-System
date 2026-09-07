package dansplugins.spawnsystem.listeners;

import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.utils.BlockChecker;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class PlayerInteractListenerTest {

    private final BlockChecker blockChecker = mock(BlockChecker.class);
    private final PersistentData persistentData = mock(PersistentData.class);
    private final Logger logger = mock(Logger.class);
    private final PlayerInteractListener playerInteractListener =
            new PlayerInteractListener(blockChecker, persistentData, logger);

    private final Player player = mock(Player.class);
    private final World world = mock(World.class);

    @Test
    void handle_spawnSignWithReadableCoordinates_setsTheSpawnInThePlayersOwnWorld() {
        PlayerInteractEvent event = rightClickOn(spawnSign("[Spawn]", "100", "64", "-200"));

        playerInteractListener.handle(event);

        verify(persistentData).setPlayersSpawn(player, world, 100, 64, -200);
    }

    @Test
    void handle_interactionWithNoBlock_isIgnored() {
        PlayerInteractEvent event =
                new PlayerInteractEvent(player, Action.RIGHT_CLICK_AIR, null, null, BlockFace.SELF);

        playerInteractListener.handle(event);

        verifyNoInteractions(blockChecker);
        verifyNoInteractions(persistentData);
    }

    @Test
    void handle_blockThatIsNotASign_isIgnored() {
        Block block = mock(Block.class);
        when(blockChecker.isSign(block)).thenReturn(false);

        playerInteractListener.handle(rightClickOn(block));

        verifyNoInteractions(persistentData);
    }

    @Test
    void handle_signWithoutTheSpawnTag_isIgnored() {
        PlayerInteractEvent event = rightClickOn(spawnSign("Welcome!", "100", "64", "-200"));

        playerInteractListener.handle(event);

        verifyNoInteractions(persistentData);
    }

    @Test
    void handle_unreadableCoordinate_setsNoSpawnAndTellsThePlayerWhy() {
        PlayerInteractEvent event = rightClickOn(spawnSign("[Spawn]", "100", "not-a-number", "-200"));

        playerInteractListener.handle(event);

        verifyNoInteractions(persistentData);
        verify(player).sendMessage(ChatColor.RED + "Sorry! The coordinates on this spawn sign couldn't be read. Please see an admin for assistance.");
    }

    // Coordinates are read with Integer.parseInt, so a decimal written on the sign is rejected outright
    // rather than being truncated to a block coordinate.
    @Test
    void handle_decimalCoordinate_setsNoSpawnAndTellsThePlayerWhy() {
        PlayerInteractEvent event = rightClickOn(spawnSign("[Spawn]", "100", "64.5", "-200"));

        playerInteractListener.handle(event);

        verifyNoInteractions(persistentData);
        verify(player).sendMessage(ChatColor.RED + "Sorry! The coordinates on this spawn sign couldn't be read. Please see an admin for assistance.");
    }

    @Test
    void handle_unreadableCoordinate_warnsThroughThePluginLoggerWithTheCause() {
        Block block = spawnSign("[Spawn]", "100", "not-a-number", "-200");
        when(block.getX()).thenReturn(10);
        when(block.getY()).thenReturn(70);
        when(block.getZ()).thenReturn(-30);
        when(world.getName()).thenReturn("world");

        playerInteractListener.handle(rightClickOn(block));

        verify(logger).log(
                eq(Level.WARNING),
                contains("spawn selection sign located at [10, 70, -30] in world"),
                any(NumberFormatException.class));
    }

    @Test
    void handle_leftClickOnASpawnSign_setsNoSpawn() {
        Block block = spawnSign("[Spawn]", "100", "64", "-200");
        PlayerInteractEvent event =
                new PlayerInteractEvent(player, Action.LEFT_CLICK_BLOCK, null, block, BlockFace.NORTH);

        playerInteractListener.handle(event);

        verifyNoInteractions(persistentData);
    }

    // A pressure plate or tripwire produces a PHYSICAL interaction, which carries a clicked block but is
    // not a deliberate selection.
    @Test
    void handle_physicalInteractionWithASpawnSign_setsNoSpawn() {
        Block block = spawnSign("[Spawn]", "100", "64", "-200");
        PlayerInteractEvent event =
                new PlayerInteractEvent(player, Action.PHYSICAL, null, block, BlockFace.NORTH);

        playerInteractListener.handle(event);

        verifyNoInteractions(persistentData);
    }

    private Block spawnSign(String firstLine, String x, String y, String z) {
        Sign sign = mock(Sign.class);
        when(sign.getLine(0)).thenReturn(firstLine);
        when(sign.getLine(1)).thenReturn(x);
        when(sign.getLine(2)).thenReturn(y);
        when(sign.getLine(3)).thenReturn(z);

        Block block = mock(Block.class);
        when(block.getState()).thenReturn(sign);
        when(blockChecker.isSign(block)).thenReturn(true);
        return block;
    }

    private PlayerInteractEvent rightClickOn(Block block) {
        when(player.getWorld()).thenReturn(world);
        return new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, null, block, BlockFace.NORTH);
    }
}
