package dansplugins.spawnsystem;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInterpreter {

    public boolean interpretCommand(CommandSender sender, String label, String[] args) {

        if (label.equalsIgnoreCase("resetspawn")) {

            // if sender instanceof player
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if (args.length > 0) {
                    if (player.hasPermission("dansplugins.spawnsystem.reset.others") || player.hasPermission("dansplugins.spawnsystem.admin")) {
                        // reset a specific player's spawn
                        String targetPlayer = args[0];
                        UtilitySubsystem.getInstance().resetSpawn(UtilitySubsystem.findUUIDBasedOnPlayerName(targetPlayer));
                        player.sendMessage(ChatColor.GREEN + "Spawn reset for " + targetPlayer + "!");
                        try {
                            DansSpawnSystem.getInstance().getServer().getPlayer(targetPlayer).sendMessage(ChatColor.GREEN + "Your spawn has been reset!");
                        } catch(Exception e) {

                        }
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need the following permission: 'dansplugins.spawnsystem.reset.others'");
                    }

                }
                else {
                    if (player.hasPermission("dansplugins.spawnsystem.reset.self") || player.hasPermission("dansplugins.spawnsystem.admin")) {
                        // reset the spawn of the command sender
                        UtilitySubsystem.getInstance().resetSpawn(UtilitySubsystem.findUUIDBasedOnPlayerName(player.getName()));
                        player.sendMessage(ChatColor.GREEN + "You have reset your spawn!");
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need the following permission: 'dansplugins.spawnsystem.reset.self'");
                    }
                }
            }

        }

        return false;
    }

}
