package spawnsystem.EventHandlers;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import spawnsystem.Main;

public class BlockBreakEventHandler {

    Main main = null;

    public BlockBreakEventHandler(Main plugin) {
        main = plugin;
    }

    public void handle(BlockBreakEvent event) {
        if (main.utilities.isSign(event.getBlock())) {

            Sign sign = (Sign) event.getBlock().getState();
            if (sign.getLine(0).contains("[Spawn]")) {

                if (event.getPlayer().hasPermission("spawnsystem.breakSpawnSign") || event.getPlayer().hasPermission("spawnsystem.admin")) {
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Spawn selection sign broken!");
                }
                else {
                    event.getPlayer().sendMessage(ChatColor.RED + "Sorry! In order to break a spawn selection sign, you must have the following permission: 'spawnsystem.breakSpawnSign");
                }

            }

        }
    }

}
