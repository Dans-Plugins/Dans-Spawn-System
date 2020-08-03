package economysystem.Subsystems;

import economysystem.Commands.BalanceCommand;
import economysystem.Commands.DepositCommand;
import economysystem.Commands.EconCommand;
import economysystem.Commands.WithdrawCommand;
import economysystem.Main;
import org.bukkit.command.CommandSender;

public class CommandSubsystem {

    Main main = null;

    public CommandSubsystem(Main plugin) {
        main = plugin;
    }

    public boolean interpretCommand(CommandSender sender, String label, String[] args) {

        if (label.equalsIgnoreCase("start")) {

            if (sender instanceof Player) {

                Player player = (Player) sender;

                // if hasn't already chosen subculture.
                if (playerSpawns.containsKey(player.getName())) {
                    player.sendMessage(ChatColor.RED + "You already went through the start process!");
                    return false;
                }

                player.sendMessage(ChatColor.GREEN + "Before you can start playing, we need to know where to set your spawn!");
                player.sendMessage(ChatColor.GREEN + "What race would you like to be?\nChoices: \n* Human\n* Greyfolk\n* Horndall\n* Felkata.");
                player.sendMessage(ChatColor.GREEN + "Type /race (race selection) to continue.");
            }
        }

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
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("horndall")) {
                        int x = 3728;
                        int y = 100;
                        int z = -1232;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (raceSelection.equalsIgnoreCase("felkata")) {
                        int x = 2222;
                        int y = 84;
                        int z = 1788;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
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

        if (label.equalsIgnoreCase("subculture")) {
            if (sender instanceof Player) {

                Player player = (Player) sender;

                if (args.length > 0) {

                    String selection = args[0];

                    int x, y, z;

                    // set spawn
                    if (selection.equalsIgnoreCase(subcultures[0])) {
                        x = -3394;
                        y = 66;
                        z = 61;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[1])) {
                        x = -2785;
                        y = 85;
                        z = 1219;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[2])) {
                        x = -1966;
                        y = 142;
                        z = -1841;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
                        }
                        return true;
                    }
                    if (selection.equalsIgnoreCase(subcultures[3])) {
                        x = 473;
                        y = 74;
                        z = 437;
                        teleportIfOverriding(args, player, x, y, z);
                        if (args.length == 1) {
                            setPlayersSpawn(player, x, y, z);
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

                player.sendMessage(ChatColor.AQUA + "Humans: " + subcultures[0] + ", " + subcultures[1] + ", " + subcultures[2] + ", " + subcultures[3]);
                player.sendMessage(ChatColor.AQUA + "Greyfolk: " + subcultures[4] + ", " + subcultures[5] + ", " + subcultures[6] + ", " + subcultures[7]);
                player.sendMessage(ChatColor.AQUA + "Horndall: " + subcultures[8] + ", " + subcultures[9] + ", " + subcultures[10] + ", " + subcultures[11]);
                player.sendMessage(ChatColor.AQUA + "Felkata: " + subcultures[12] + ", " + subcultures[13] + ", " + subcultures[14] + ", " + subcultures[15]);

            }

        }

        return false;
    }

}
