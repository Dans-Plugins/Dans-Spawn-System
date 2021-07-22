package dansplugins.spawnsystem.data;

import org.bukkit.Location;

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

}
