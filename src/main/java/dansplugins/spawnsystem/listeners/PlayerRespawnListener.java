package dansplugins.spawnsystem.listeners;

import dansplugins.spawnsystem.DansSpawnSystem;
import dansplugins.spawnsystem.data.PersistentData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {
    private final PersistentData persistentData;
    private final DansSpawnSystem dansSpawnSystem;

    public PlayerRespawnListener(PersistentData persistentData, DansSpawnSystem dansSpawnSystem) {
        this.persistentData = persistentData;
        this.dansSpawnSystem = dansSpawnSystem;
    }

    @EventHandler()
    public void handle(PlayerRespawnEvent event) {
        if (playerHasBedSpawn(event.getPlayer())) {
            return;
        }

        if (persistentData.getPlayerSpawns().containsKey(event.getPlayer().getUniqueId())) {
            dansSpawnSystem.getServer().getScheduler().runTaskLater(dansSpawnSystem, new Runnable() {
                @Override
                public void run() {
                    event.getPlayer().teleport(persistentData.getPlayerSpawns().get(event.getPlayer().getUniqueId()));
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Teleporting to custom spawn!");
                }
            }, 1);
        }
    }

    private boolean playerHasBedSpawn(Player player) {
        return player.getBedSpawnLocation() != null;
    }

}
