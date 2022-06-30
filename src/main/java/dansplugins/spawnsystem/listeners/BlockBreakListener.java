package dansplugins.spawnsystem.listeners;

import dansplugins.spawnsystem.utils.BlockChecker;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class BlockBreakListener implements Listener {
    private final BlockChecker blockChecker;

    public BlockBreakListener(BlockChecker blockChecker) {
        this.blockChecker = blockChecker;
    }

    @EventHandler()
    public void handle(BlockBreakEvent event) {
        if (blockChecker.isSign(event.getBlock())) {

            Sign sign = (Sign) event.getBlock().getState();
            if (sign.getLine(0).contains("[Spawn]")) {

                if (event.getPlayer().hasPermission("spawnsystem.breakSpawnSign") || event.getPlayer().hasPermission("spawnsystem.admin")) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Spawn selection sign broken!");
                }
                else {
                    event.getPlayer().sendMessage(ChatColor.RED + "Sorry! In order to break a spawn selection sign, you must have the following permission: 'spawnsystem.breakSpawnSign'");
                    event.setCancelled(true);
                }

            }

        }
        else {
            if (isNextToSpawnSelectionSign(event.getBlock())) {

                if (event.getPlayer().hasPermission("spawnsystem.breakSpawnSign") || event.getPlayer().hasPermission("spawnsystem.admin")) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Spawn selection sign broken!");
                }
                else {
                    event.getPlayer().sendMessage(ChatColor.RED + "Sorry! In order to break a spawn selection sign, you must have the following permission: 'spawnsystem.breakSpawnSign'");
                    event.setCancelled(true);
                }

            }
        }
    }

    private boolean isNextToSpawnSelectionSign(Block block) {
        // declare blocks
        ArrayList<Block> blocks = new ArrayList<>();
        blocks.add(block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ()));
        blocks.add(block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()));
        blocks.add(block.getWorld().getBlockAt(block.getX() + 1, block.getY(), block.getZ()));
        blocks.add(block.getWorld().getBlockAt(block.getX() - 1, block.getY(), block.getZ()));
        blocks.add(block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() + 1));
        blocks.add(block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() - 1));

        for (Block b : blocks) {
            if (blockChecker.isSign(b)) {
                Sign sign = (Sign) b.getState();
                if (sign.getLine(0).contains("[Spawn]")) {
                    return true;
                }
            }
        }

        return false;
    }

}
