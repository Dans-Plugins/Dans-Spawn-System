package dansplugins.spawnsystem.listeners;

import dansplugins.spawnsystem.data.PersistentData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class PlayerRespawnListener implements Listener {
    private final PersistentData persistentData;
    private final Plugin plugin;

    public PlayerRespawnListener(PersistentData persistentData, Plugin plugin) {
        this.persistentData = persistentData;
        this.plugin = plugin;
    }

    @EventHandler()
    public void handle(PlayerRespawnEvent event) {
        if (playerHasBedSpawn(event.getPlayer())) {
            return;
        }

        if (persistentData.getPlayerSpawns().containsKey(event.getPlayer().getUniqueId())) {
            plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    // the spawn may have been reset with /resetspawn since the respawn
                    Location customSpawn = persistentData.getPlayerSpawns().get(event.getPlayer().getUniqueId());
                    if (customSpawn == null) {
                        return;
                    }
                    event.getPlayer().teleport(customSpawn);
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Teleporting to custom spawn!");
                }
            }, 1);
        }
    }

    private boolean playerHasBedSpawn(Player player) {
        return player.getBedSpawnLocation() != null;
    }

}
