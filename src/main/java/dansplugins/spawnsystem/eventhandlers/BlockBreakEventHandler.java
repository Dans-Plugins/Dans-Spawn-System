package dansplugins.spawnsystem.eventhandlers;

import dansplugins.spawnsystem.UtilitySubsystem;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;

public class BlockBreakEventHandler implements Listener {

    @EventHandler()
    public void handle(BlockBreakEvent event) {
        if (UtilitySubsystem.getInstance().isSign(event.getBlock())) {

            Sign sign = (Sign) event.getBlock().getState();
            if (sign.getLine(0).contains("[Spawn]")) {

                if (event.getPlayer().hasPermission("dansplugins.spawnsystem.breakSpawnSign") || event.getPlayer().hasPermission("dansplugins.spawnsystem.admin")) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Spawn selection sign broken!");
                }
                else {
                    event.getPlayer().sendMessage(ChatColor.RED + "Sorry! In order to break a spawn selection sign, you must have the following permission: 'dansplugins.spawnsystem.breakSpawnSign");
                    event.setCancelled(true);
                }

            }

        }
        else {
            if (isNextToSpawnSelectionSign(event.getBlock())) {

                if (event.getPlayer().hasPermission("dansplugins.spawnsystem.breakSpawnSign") || event.getPlayer().hasPermission("dansplugins.spawnsystem.admin")) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Spawn selection sign broken!");
                }
                else {
                    event.getPlayer().sendMessage(ChatColor.RED + "Sorry! In order to break a spawn selection sign, you must have the following permission: 'dansplugins.spawnsystem.breakSpawnSign");
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
            if (UtilitySubsystem.getInstance().isSign(b)) {
                Sign sign = (Sign) b.getState();
                if (sign.getLine(0).contains("[Spawn]")) {
                    return true;
                }
            }
        }

        return false;
    }

}
