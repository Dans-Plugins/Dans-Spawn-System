package spawnsystem.EventHandlers;

import org.bukkit.ChatColor;

import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockPlaceEvent;
import spawnsystem.Main;

public class BlockPlaceEventHandler {

    Main main = null;

    public BlockPlaceEventHandler(Main plugin) {
        main = plugin;
    }

    public void handle(BlockPlaceEvent event) {

        // if trying to place sign
        if (main.utilities.isSign(event.getBlockPlaced())) {
            Sign sign = (Sign) event.getBlockPlaced();

            // check if it contains says [Spawn]
            if (sign.getLine(0).contains("[Spawn]")) {
                // if it does, check if the player has permission
                if (!(event.getPlayer().hasPermission("spawnsystem.placeSpawnSign") || event.getPlayer().hasPermission("spawnsystem.admin"))) {
                    // if they don't, cancel the event with a message
                    event.getPlayer().sendMessage(ChatColor.RED + "Sorry! In order to place a spawn selection sign, you must have the following permission: 'spawnsystem.placeSpawnSign");
                    event.setCancelled(true);
                }

            }

        }



    }

}
