package dansplugins.spawnsystem.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeEventListener implements Listener {

    @EventHandler()
    public void handle(SignChangeEvent event) {
        // check if it contains says [Spawn]
        if (event.getLine(0).contains("[Spawn]")) {
            // if it does, check if the player has permission
            if (event.getPlayer().hasPermission("spawnsystem.placeSpawnSign") || event.getPlayer().hasPermission("spawnsystem.admin")) {
                event.getPlayer().sendMessage(ChatColor.GREEN + "Spawn selection sign created!");
            }
            else {
                // if they don't, cancel the event with a message
                event.getPlayer().sendMessage(ChatColor.RED + "Sorry! In order to place a spawn selection sign, you must have the following permission: 'spawnsystem.placeSpawnSign'");
                event.setCancelled(true);
            }
        }
    }
}
