package dansplugins.spawnsystem.utils;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayers;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class UUIDChecker {

    // Pasarus wrote this
    public UUID findUUIDBasedOnPlayerName(String playerName) {
        // Check online
        for (Player player : getOnlinePlayers()){
            if (player.getName().equals(playerName)){
                return player.getUniqueId();
            }
        }

        // Check offline
        for (OfflinePlayer player : getOfflinePlayers()){
            try {
                if (player.getName().equals(playerName)){
                    return player.getUniqueId();
                }
            } catch (NullPointerException e) {
                // Fail silently as quit possibly common.
            }

        }

        return null;
    }

}
