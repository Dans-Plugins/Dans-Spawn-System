package dansplugins.spawnsystem.listeners;

import dansplugins.spawnsystem.utils.BlockChecker;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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

        verify(player).sendMessage(ChatColor.RED + "Sorry! In order to break a spawn selection sign, you must have the following permission: 'spawnsystem.breakSpawnSign'");
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

    // USER_GUIDE.md: [Spawn] is case-sensitive, so a sign written any other way isn't protected.
    @ParameterizedTest
    @ValueSource(strings = {"[spawn]", "[SPAWN]", "Spawn"})
    void handle_signWithTheSpawnTagWrittenDifferentlyBroken_isLeftAlone(String firstLine) {
        Player player = mock(Player.class);
        BlockBreakEvent event = new BlockBreakEvent(signWithFirstLine(firstLine), player);

        blockBreakListener.handle(event);

        verify(player, never()).sendMessage(anyString());
        assertFalse(event.isCancelled());
    }

    // USER_GUIDE.md: other text around [Spawn] on the first line is allowed, and such a sign is protected too.
    @Test
    void handle_spawnTagSurroundedByOtherTextBrokenWithoutPermission_isCancelledWithAnExplanation() {
        Player player = mock(Player.class);
        BlockBreakEvent event = new BlockBreakEvent(signWithFirstLine("Town [Spawn]"), player);

        blockBreakListener.handle(event);

        verify(player).sendMessage(ChatColor.RED + "Sorry! In order to break a spawn selection sign, you must have the following permission: 'spawnsystem.breakSpawnSign'");
        assertTrue(event.isCancelled());
    }

    // USER_GUIDE.md's exception: only a [Spawn] sign is protected, so a sign doesn't inherit protection
    // from the [Spawn] sign beside it the way any other block would.
    @Test
    void handle_ordinarySignTouchingASpawnSign_isLeftAlone() {
        Player player = mock(Player.class);
        Block ordinarySign = signWithFirstLine("Welcome!");
        when(ordinarySign.getWorld()).thenReturn(world);
        when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(mock(Block.class));
        neighbourAt(1, 0, 0, spawnSign());
        BlockBreakEvent event = new BlockBreakEvent(ordinarySign, player);

        blockBreakListener.handle(event);

        verify(player, never()).sendMessage(anyString());
        assertFalse(event.isCancelled());
    }

    // A non-sign block is still protected when a spawn sign sits in any of the six blocks around it,
    // which is what stops the supporting block being knocked out from under a spawn sign.
    @ParameterizedTest
    @CsvSource({"0, 1, 0", "0, -1, 0", "1, 0, 0", "-1, 0, 0", "0, 0, 1", "0, 0, -1"})
    void handle_blockNextToASpawnSignBrokenWithoutPermission_isCancelledWithAnExplanation(int x, int y, int z) {
        Player player = mock(Player.class);
        Block block = plainBlockAtOrigin();
        neighbourAt(x, y, z, spawnSign());
        BlockBreakEvent event = new BlockBreakEvent(block, player);

        blockBreakListener.handle(event);

        verify(player).sendMessage(ChatColor.RED + "Sorry! In order to break a block next to a spawn selection sign, you must have the following permission: 'spawnsystem.breakSpawnSign'");
        assertTrue(event.isCancelled());
    }

    // The sign falls only if it is attached to the block, so the confirmation can't claim that it was broken.
    @ParameterizedTest
    @ValueSource(strings = {"spawnsystem.breakSpawnSign", "spawnsystem.admin"})
    void handle_blockNextToASpawnSignBrokenWithPermission_isConfirmedAndAllowed(String permission) {
        Player player = playerWithPermission(permission);
        Block block = plainBlockAtOrigin();
        neighbourAt(0, -1, 0, spawnSign());
        BlockBreakEvent event = new BlockBreakEvent(block, player);

        blockBreakListener.handle(event);

        verify(player).sendMessage(ChatColor.GREEN + "Block next to a spawn selection sign broken!");
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
