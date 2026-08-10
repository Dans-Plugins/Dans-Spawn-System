package dansplugins.spawnsystem.commands;

import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.utils.UUIDChecker;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ResetSpawnCommand {
    private final PersistentData persistentData;
    private final UUIDChecker uuidChecker;
    private final Server server;

    public ResetSpawnCommand(PersistentData persistentData, UUIDChecker uuidChecker, Server server) {
        this.persistentData = persistentData;
        this.uuidChecker = uuidChecker;
        this.server = server;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Sorry! This command can only be used by an in-game player.");
            return;
        }

        Player player = (Player) sender;

        if (args.length > 0) {
            resetSpawnOfOtherPlayer(player, args[0]);
        }
        else {
            resetSpawnOfSender(player);
        }
    }

    private void resetSpawnOfOtherPlayer(Player player, String targetPlayerName) {
        if (!player.hasPermission("spawnsystem.reset.others") && !player.hasPermission("spawnsystem.admin")) {
            player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need the following permission: 'spawnsystem.reset.others'");
            return;
        }

        UUID targetPlayerUUID = uuidChecker.findUUIDBasedOnPlayerName(targetPlayerName);
        if (targetPlayerUUID == null) {
            player.sendMessage(ChatColor.RED + "Sorry! A player named '" + targetPlayerName + "' could not be found.");
            return;
        }

        persistentData.resetSpawn(targetPlayerUUID);
        player.sendMessage(ChatColor.GREEN + "Spawn reset for " + targetPlayerName + "!");

        // the target is only notified when they happen to be online
        Player targetPlayer = server.getPlayer(targetPlayerUUID);
        if (targetPlayer != null) {
            targetPlayer.sendMessage(ChatColor.GREEN + "Your spawn has been reset!");
        }
    }

    private void resetSpawnOfSender(Player player) {
        if (!player.hasPermission("spawnsystem.reset.self") && !player.hasPermission("spawnsystem.admin")) {
            player.sendMessage(ChatColor.RED + "Sorry! In order to use this command, you need the following permission: 'spawnsystem.reset.self'");
            return;
        }

        persistentData.resetSpawn(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "You have reset your spawn!");
    }

}
