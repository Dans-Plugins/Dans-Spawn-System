package spawnsystem.Subsystems;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import spawnsystem.Main;

public class CommandSubsystem {

    Main main = null;

    public CommandSubsystem(Main plugin) {
        main = plugin;
    }

    public boolean interpretCommand(CommandSender sender, String label, String[] args) {

        if (label.equalsIgnoreCase("resetspawn")) {

            // if sender instanceof player
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length > 0) {
                    if (player.hasPermission("spawnsystem.reset.others") || player.hasPermission("spawnsystem.admin")) {
                        // reset a specific player's spawn
                        String targetPlayer = args[0];
                        main.utilities.resetSpawn(targetPlayer);
                        player.sendMessage(ChatColor.GREEN + "Spawn reset for " + targetPlayer + "!");
                        try {
                            main.getServer().getPlayer(targetPlayer).sendMessage(ChatColor.GREEN + "Your spawn has been reset!");
                        } catch(Exception e) {

                        }
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need the following permission: 'spawnsystem.reset.others'");
                    }

                }
                else {
                    if (player.hasPermission("spawnsystem.reset.self") || player.hasPermission("spawnsystem.admin")) {
                        // reset the spawn of the command sender
                        main.utilities.resetSpawn(player.getName());
                        player.sendMessage(ChatColor.GREEN + "You have reset your spawn!");
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need the following permission: 'spawnsystem.reset.self'");
                    }
                }
            }

        }

        // ----------------------------------------------------------------------------------------------------------------------------------------------
        // OLD COMMANDS

        if (label.equalsIgnoreCase("start")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                // if hasn't already chosen subculture.
                if (main.playerSpawns.containsKey(player.getName())) {
                    player.sendMessage(ChatColor.RED + "You already went through the start process!");
                    return false;
                }

                player.sendMessage(ChatColor.GREEN + "Before you can start playing, we need to know where to set your spawn!");
                player.sendMessage(ChatColor.GREEN + "What race would you like to be?\nChoices: \n* Human\n* Greyfolk\n* Horndall\n* Felkata.");
                player.sendMessage(ChatColor.GREEN + "Type /race (race selection) to continue.");
            }
        }

        // if players don't choose human, their spawns are immediately set in this if statement
        if (label.equalsIgnoreCase("race")) {
            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (args.length > 0) {

                    String raceSelection = args[0];

                    if (raceSelection.equalsIgnoreCase("human")) {
                        player.sendMessage(ChatColor.GREEN + "What subculture of Human are you?\nChoices: \n* Ostendian\n* Massara\n* Njord'volk\n* La'Vanti.");
                        player.sendMessage(ChatColor.GREEN + "Type /subculture (subculture selection) to continue.");
                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("greyfolk")) {
                        int x = -136;
                        int y = 87;
                        int z = 138;
                        main.utilities.teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            main.utilities.setPlayersSpawn(player, player.getWorld(), x, y, z);
                        }
                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("horndall")) {
                        int x = 3728;
                        int y = 100;
                        int z = -1232;
                        main.utilities.teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            main.utilities.setPlayersSpawn(player, player.getWorld(), x, y, z);
                        }
                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("felkata")) {
                        int x = 2222;
                        int y = 84;
                        int z = 1788;
                        main.utilities.teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            main.utilities.setPlayersSpawn(player, player.getWorld(), x, y, z);
                        }
                        return true;
                    }

                    // not a valid race
                    player.sendMessage(ChatColor.RED + "That isn't a valid race!");

                }
                else {
                    player.sendMessage(ChatColor.RED + "Usage: /race (race selection)");
                    player.sendMessage(ChatColor.RED + "The races are Human, Greyfolk, Horndall and Felkata.");
                }

            }
        }

        // this if statement should only ever run for players choosing the human race
        if (label.equalsIgnoreCase("subculture")) {
            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (args.length > 0) {

                    String selection = args[0];

                    int x, y, z;

                    // set spawn
                    if (selection.equalsIgnoreCase(main.subcultures[0])) {
                        x = -3394;
                        y = 66;
                        z = 61;
                        main.utilities.teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            main.utilities.setPlayersSpawn(player, player.getWorld(), x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(main.subcultures[1])) {
                        x = -2785;
                        y = 85;
                        z = 1219;
                        main.utilities.teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            main.utilities.setPlayersSpawn(player, player.getWorld(), x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(main.subcultures[2])) {
                        x = -1966;
                        y = 142;
                        z = -1841;
                        main.utilities.teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            main.utilities.setPlayersSpawn(player, player.getWorld(), x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(main.subcultures[3])) {
                        x = 473;
                        y = 74;
                        z = 437;
                        main.utilities.teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            main.utilities.setPlayersSpawn(player, player.getWorld(), x, y, z);
                        }
                        return true;
                    }

                    // not a valid subculture
                    player.sendMessage(ChatColor.RED + "That isn't a valid subculture! Type /subcultures to view all subcultures.");

                }

            }
        }

        if (label.equalsIgnoreCase("subcultures")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                player.sendMessage(ChatColor.AQUA + "Humans: " + main.subcultures[0] + ", " + main.subcultures[1] + ", " + main.subcultures[2] + ", " + main.subcultures[3]);
                player.sendMessage(ChatColor.AQUA + "Greyfolk: " + main.subcultures[4] + ", " + main.subcultures[5] + ", " + main.subcultures[6] + ", " + main.subcultures[7]);
                player.sendMessage(ChatColor.AQUA + "Horndall: " + main.subcultures[8] + ", " + main.subcultures[9] + ", " + main.subcultures[10] + ", " + main.subcultures[11]);
                player.sendMessage(ChatColor.AQUA + "Felkata: " + main.subcultures[12] + ", " + main.subcultures[13] + ", " + main.subcultures[14] + ", " + main.subcultures[15]);

            }

        }

        return false;
    }

}
