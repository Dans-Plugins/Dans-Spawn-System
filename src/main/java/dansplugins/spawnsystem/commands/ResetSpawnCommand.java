package dansplugins.spawnsystem.commands;

import dansplugins.spawnsystem.DansSpawnSystem;
import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.utils.UUIDChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetSpawnCommand {
    private final PersistentData persistentData;
    private final UUIDChecker uuidChecker;
    private final DansSpawnSystem dansSpawnSystem;

    public ResetSpawnCommand(PersistentData persistentData, UUIDChecker uuidChecker, DansSpawnSystem dansSpawnSystem) {
        this.persistentData = persistentData;
        this.uuidChecker = uuidChecker;
        this.dansSpawnSystem = dansSpawnSystem;
    }

    public void execute(CommandSender sender, String[] args) {
        // if sender instanceof player
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length > 0) {
                if (player.hasPermission("spawnsystem.reset.others") || player.hasPermission("spawnsystem.admin")) {
                    // reset a specific player's spawn
                    String targetPlayer = args[0];
                    persistentData.resetSpawn(uuidChecker.findUUIDBasedOnPlayerName(targetPlayer));
                    player.sendMessage(ChatColor.GREEN + "Spawn reset for " + targetPlayer + "!");
                    try {
                        dansSpawnSystem.getServer().getPlayer(targetPlayer).sendMessage(ChatColor.GREEN + "Your spawn has been reset!");
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
                    persistentData.resetSpawn(uuidChecker.findUUIDBasedOnPlayerName(player.getName()));
                    player.sendMessage(ChatColor.GREEN + "You have reset your spawn!");
                }
                else {
                    player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need the following permission: 'spawnsystem.reset.self'");
                }
            }
        }
    }

}
