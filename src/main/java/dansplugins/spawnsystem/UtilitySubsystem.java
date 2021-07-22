package dansplugins.spawnsystem;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import dansplugins.spawnsystem.DansSpawnSystem;

import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayers;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class UtilitySubsystem {

    private static UtilitySubsystem instance;

    private UtilitySubsystem() {

    }

    public static UtilitySubsystem getInstance() {
        if (instance == null) {
            instance = new UtilitySubsystem();
        }
        return instance;
    }

    public void setPlayersSpawn(Player player, World world, int x, int y, int z) {

        Location spawnLocation = new Location(world, x, y, z);

        // set spawn
        if (!DansSpawnSystem.getInstance().playerSpawns.containsKey(player.getName())) {
            DansSpawnSystem.getInstance().playerSpawns.put(player.getUniqueId(), spawnLocation);
            DansSpawnSystem.getInstance().playersWithSpawns.add(player.getUniqueId());
        }
        else {
            player.sendMessage(ChatColor.RED + "You have already set your spawn! If you're starting a new character please see an admin for assistance.");
            return;
        }


        // teleport player
        player.teleport(spawnLocation);

        player.sendMessage(ChatColor.GREEN + "Spawn set!");

    }

    public void teleportIfOverriding(String[] args, Player player, int x, int y, int z) {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("override")) {
                if (player.hasPermission("dansplugins.spawnsystem.override") || player.hasPermission("dansplugins.spawnsystem.admin")) {
                    Location teleportLocation = new Location(player.getWorld(), x, y, z);
                    player.teleport(teleportLocation);
                }
                else {
                    player.sendMessage(ChatColor.RED + "Sorry! In order to use the override, you need the following permission: 'dansplugins.spawnsystem.override'");
                }
            }
        }
    }

    public boolean isSign(Block block) {
        switch(block.getType()) {
            case ACACIA_SIGN:
            case ACACIA_WALL_SIGN:
            case BIRCH_SIGN:
            case BIRCH_WALL_SIGN:
            case DARK_OAK_SIGN:
            case DARK_OAK_WALL_SIGN:
            case JUNGLE_SIGN:
            case JUNGLE_WALL_SIGN:
            case OAK_SIGN:
            case OAK_WALL_SIGN:
            case SPRUCE_SIGN:
            case SPRUCE_WALL_SIGN:
                return true;
        }
        return false;
    }

    public void resetSpawn(UUID player) {
        DansSpawnSystem.getInstance().playersWithSpawns.remove(player);
        DansSpawnSystem.getInstance().playerSpawns.remove(player);
    }

    // Pasarus wrote this
    public static UUID findUUIDBasedOnPlayerName(String playerName){
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
