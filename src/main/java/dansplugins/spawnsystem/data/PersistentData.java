package dansplugins.spawnsystem.data;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PersistentData {

    private static PersistentData instance;

    private HashMap<UUID, Location> playerSpawns = new HashMap<>();
    private ArrayList<UUID> playersWithSpawns = new ArrayList<>();

    private PersistentData() {

    }

    public static PersistentData getInstance() {
        if (instance == null) {
            instance = new PersistentData();
        }
        return instance;
    }

    public HashMap<UUID, Location> getPlayerSpawns() {
        return playerSpawns;
    }

    public ArrayList<UUID> getPlayersWithSpawns() {
        return playersWithSpawns;
    }

    public void setPlayersSpawn(Player player, World world, int x, int y, int z) {

        Location spawnLocation = new Location(world, x, y, z);

        // set spawn
        if (!PersistentData.getInstance().getPlayerSpawns().containsKey(player.getName())) {
            PersistentData.getInstance().getPlayerSpawns().put(player.getUniqueId(), spawnLocation);
            PersistentData.getInstance().getPlayersWithSpawns().add(player.getUniqueId());
        }
        else {
            player.sendMessage(ChatColor.RED + "You have already set your spawn! If you're starting a new character please see an admin for assistance.");
            return;
        }


        // teleport player
        player.teleport(spawnLocation);

        player.sendMessage(ChatColor.GREEN + "Spawn set!");

    }

    public void resetSpawn(UUID player) {
        PersistentData.getInstance().getPlayersWithSpawns().remove(player);
        PersistentData.getInstance().getPlayerSpawns().remove(player);
    }

}
