package spawnsystem.Subsystems;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import spawnsystem.Main;

public class UtilitySubsystem {

    Main main = null;

    public UtilitySubsystem(Main plugin) {
        main = plugin;
    }

    public void setPlayersSpawn(Player player, int x, int y, int z) {

        Location spawnLocation = new Location(main.getServer().getWorld(main.worldname), x, y, z);

        // set spawn
        if (!main.playerSpawns.containsKey(player.getName())) {
            main.playerSpawns.put(player.getName(), spawnLocation);
            main.playersWithSpawns.add(player.getName());
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
                if (player.hasPermission("spawnsystem.override")) {
                    Location teleportLocation = new Location(main.getServer().getWorld(main.worldname), x, y, z);
                    player.teleport(teleportLocation);
                }
                else {
                    player.sendMessage(ChatColor.RED + "Sorry! In order to use the override, you need the following permission: 'spawnsystem.override'");
                }
            }
        }
    }


}
