package spawnsystem.EventHandlers;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerRespawnEvent;
import spawnsystem.Main;

public class PlayerRespawnEventHandler {

    Main main = null;

    public PlayerRespawnEventHandler(Main plugin) {
        main = plugin;
    }

    public void handle(PlayerRespawnEvent event) {
        if (main.playerSpawns.containsKey(event.getPlayer().getUniqueId())) {
            main.getServer().getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().teleport(main.playerSpawns.get(event.getPlayer().getUniqueId()));
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Teleporting to custom spawn!");
                }
            }, 1);
        }
    }

}
