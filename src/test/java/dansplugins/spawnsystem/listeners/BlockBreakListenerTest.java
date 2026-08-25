package dansplugins.spawnsystem.listeners;

import dansplugins.spawnsystem.utils.BlockChecker;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BlockBreakListenerTest {

    private final BlockChecker blockChecker = mock(BlockChecker.class);
    private final BlockBreakListener blockBreakListener = new BlockBreakListener(blockChecker);

    private final World world = mock(World.class);

    @Test
    void handle_spawnSignBrokenWithBreakPermission_isConfirmedAndAllowed() {
        Player player = playerWithPermission("spawnsystem.breakSpawnSign");
        BlockBreakEvent event = new BlockBreakEvent(spawnSign(), player);

        blockBreakListener.handle(event);

        verify(player).sendMessage(ChatColor.GREEN + "Spawn selection sign broken!");
        assertFalse(event.isCancelled());
    }

    @Test
    void handle_spawnSignBrokenWithAdminPermission_isConfirmedAndAllowed() {
        Player player = playerWithPermission("spawnsystem.admin");
        BlockBreakEvent event = new BlockBreakEvent(spawnSign(), player);

        blockBreakListener.handle(event);

        verify(player).sendMessage(ChatColor.GREEN + "Spawn selection sign broken!");
        assertFalse(event.isCancelled());
    }

    @Test
    void handle_spawnSignBrokenWithoutPermission_isCancelledWithAnExplanation() {
        Player player = mock(Player.class);
        BlockBreakEvent event = new BlockBreakEvent(spawnSign(), player);

        blockBreakListener.handle(event);

        verify(player).sendMessage(contains("spawnsystem.breakSpawnSign"));
        assertTrue(event.isCancelled());
    }

    @Test
    void handle_signWithoutTheSpawnTagBroken_isLeftAlone() {
        Player player = mock(Player.class);
        BlockBreakEvent event = new BlockBreakEvent(signWithFirstLine("Welcome!"), player);

        blockBreakListener.handle(event);

        verify(player, never()).sendMessage(anyString());
        assertFalse(event.isCancelled());
    }

    // A non-sign block is still protected when a spawn sign sits in any of the six blocks around it,
    // which is what stops the supporting block being knocked out from under a spawn sign.
    @Test
    void handle_blockSupportingASpawnSignBrokenWithoutPermission_isCancelledWithAnExplanation() {
        Player player = mock(Player.class);
        Block block = plainBlockAtOrigin();
        neighbourAt(0, 1, 0, spawnSign());
        BlockBreakEvent event = new BlockBreakEvent(block, player);

        blockBreakListener.handle(event);

        verify(player).sendMessage(contains("spawnsystem.breakSpawnSign"));
        assertTrue(event.isCancelled());
    }

    @Test
    void handle_blockSupportingASpawnSignBrokenWithPermission_isConfirmedAndAllowed() {
        Player player = playerWithPermission("spawnsystem.breakSpawnSign");
        Block block = plainBlockAtOrigin();
        neighbourAt(0, -1, 0, spawnSign());
        BlockBreakEvent event = new BlockBreakEvent(block, player);

        blockBreakListener.handle(event);

        verify(player).sendMessage(ChatColor.GREEN + "Spawn selection sign broken!");
        assertFalse(event.isCancelled());
    }

    @Test
    void handle_blockNextToAnOrdinarySign_isLeftAlone() {
        Player player = mock(Player.class);
        Block block = plainBlockAtOrigin();
        neighbourAt(1, 0, 0, signWithFirstLine("Welcome!"));
        BlockBreakEvent event = new BlockBreakEvent(block, player);

        blockBreakListener.handle(event);

        verify(player, never()).sendMessage(anyString());
        assertFalse(event.isCancelled());
    }

    @Test
    void handle_blockWithNoSignsAround_isLeftAlone() {
        Player player = mock(Player.class);
        BlockBreakEvent event = new BlockBreakEvent(plainBlockAtOrigin(), player);

        blockBreakListener.handle(event);

        verify(player, never()).sendMessage(anyString());
        assertFalse(event.isCancelled());
    }

    private Block plainBlockAtOrigin() {
        Block block = mock(Block.class);
        when(blockChecker.isSign(block)).thenReturn(false);
        when(block.getWorld()).thenReturn(world);
        // Every surrounding position defaults to a non-sign block; individual tests override one of them.
        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(mock(Block.class));
        return block;
    }

    private void neighbourAt(int x, int y, int z, Block neighbour) {
        when(world.getBlockAt(x, y, z)).thenReturn(neighbour);
    }

    private Block spawnSign() {
        return signWithFirstLine("[Spawn]");
    }

    private Block signWithFirstLine(String firstLine) {
        Sign sign = mock(Sign.class);
        when(sign.getLine(0)).thenReturn(firstLine);

        Block block = mock(Block.class);
        when(block.getState()).thenReturn(sign);
        when(blockChecker.isSign(block)).thenReturn(true);
        return block;
    }

    private Player playerWithPermission(String permission) {
        Player player = mock(Player.class);
        when(player.hasPermission(permission)).thenReturn(true);
        return player;
    }
}
