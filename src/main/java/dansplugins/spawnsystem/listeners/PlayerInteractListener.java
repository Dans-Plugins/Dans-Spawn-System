package dansplugins.spawnsystem.listeners;

import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.utils.BlockChecker;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerInteractListener implements Listener {
    private final BlockChecker blockChecker;
    private final PersistentData persistentData;
    private final Logger logger;

    public PlayerInteractListener(BlockChecker blockChecker, PersistentData persistentData, Logger logger) {
        this.blockChecker = blockChecker;
        this.persistentData = persistentData;
        this.logger = logger;
    }

    @EventHandler()
    public void handle(PlayerInteractEvent event) {
        // a spawn is selected by right clicking only, as documented in USER_GUIDE.md
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock != null) {
            // if that block is a sign
            if (blockChecker.isSign(clickedBlock)) {
                // if that sign has [Spawn]
                Sign sign = (Sign) clickedBlock.getState();
                if (sign.getLine(0).contains("[Spawn]")) {
                    Player player = event.getPlayer();

                    // acquire coordinates
                    int x;
                    int y;
                    int z;
                    try {
                        x = Integer.parseInt(sign.getLine(1));
                        y = Integer.parseInt(sign.getLine(2));
                        z = Integer.parseInt(sign.getLine(3));
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Sorry! The coordinates on this spawn sign couldn't be read. Please see an admin for assistance.");
                        logger.log(Level.WARNING, "A problem occurred with a spawn selection sign located at [" + clickedBlock.getX() + ", " + clickedBlock.getY() + ", " + clickedBlock.getZ() + "] in " + player.getWorld().getName(), e);
                        return;
                    }

                    // set player's spawn
                    persistentData.setPlayersSpawn(player, player.getWorld(), x, y, z);
                }
            }
        }
    }

}
