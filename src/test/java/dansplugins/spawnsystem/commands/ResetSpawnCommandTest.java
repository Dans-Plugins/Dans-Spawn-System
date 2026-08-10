package dansplugins.spawnsystem.commands;

import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.utils.UUIDChecker;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ResetSpawnCommandTest {

    private final PersistentData persistentData = mock(PersistentData.class);
    private final UUIDChecker uuidChecker = mock(UUIDChecker.class);
    private final Server server = mock(Server.class);
    private final ResetSpawnCommand resetSpawnCommand = new ResetSpawnCommand(persistentData, uuidChecker, server);

    @Test
    void execute_nonPlayerSender_isToldTheCommandIsPlayerOnly() {
        CommandSender console = mock(CommandSender.class);

        resetSpawnCommand.execute(console, new String[]{});

        verify(console).sendMessage(ChatColor.RED + "Sorry! This command can only be used by an in-game player.");
        verifyNoInteractions(persistentData);
    }

    @Test
    void execute_unresolvableTargetName_reportsNotFoundAndResetsNothing() {
        Player player = playerWithPermission("spawnsystem.reset.others");
        when(uuidChecker.findUUIDBasedOnPlayerName("Ghost")).thenReturn(null);

        resetSpawnCommand.execute(player, new String[]{"Ghost"});

        verify(player).sendMessage(ChatColor.RED + "Sorry! A player named 'Ghost' could not be found.");
        verify(player, never()).sendMessage(startsWith(ChatColor.GREEN + "Spawn reset for"));
        verifyNoInteractions(persistentData);
    }

    @Test
    void execute_offlineTargetName_resetsSpawnAndConfirmsToSender() {
        Player player = playerWithPermission("spawnsystem.reset.others");
        UUID targetId = UUID.randomUUID();
        when(uuidChecker.findUUIDBasedOnPlayerName("Steve")).thenReturn(targetId);
        when(server.getPlayer(targetId)).thenReturn(null);

        resetSpawnCommand.execute(player, new String[]{"Steve"});

        verify(persistentData).resetSpawn(targetId);
        verify(player).sendMessage(ChatColor.GREEN + "Spawn reset for Steve!");
    }

    @Test
    void execute_onlineTargetName_alsoNotifiesTheTarget() {
        Player player = playerWithPermission("spawnsystem.reset.others");
        UUID targetId = UUID.randomUUID();
        Player target = mock(Player.class);
        when(uuidChecker.findUUIDBasedOnPlayerName("Steve")).thenReturn(targetId);
        when(server.getPlayer(targetId)).thenReturn(target);

        resetSpawnCommand.execute(player, new String[]{"Steve"});

        verify(persistentData).resetSpawn(targetId);
        verify(target).sendMessage(ChatColor.GREEN + "Your spawn has been reset!");
    }

    @Test
    void execute_targetNameWithoutOthersPermission_isRefused() {
        Player player = playerWithPermission("spawnsystem.reset.self");

        resetSpawnCommand.execute(player, new String[]{"Steve"});

        verify(player).sendMessage(contains("spawnsystem.reset.others"));
        verifyNoInteractions(persistentData);
        verifyNoInteractions(uuidChecker);
    }

    @Test
    void execute_noArguments_resetsTheSendersOwnSpawn() {
        Player player = playerWithPermission("spawnsystem.reset.self");
        UUID playerId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);

        resetSpawnCommand.execute(player, new String[]{});

        verify(persistentData).resetSpawn(playerId);
        verify(player).sendMessage(ChatColor.GREEN + "You have reset your spawn!");
    }

    @Test
    void execute_noArgumentsWithoutSelfPermission_isRefused() {
        Player player = playerWithPermission("spawnsystem.reset.others");

        resetSpawnCommand.execute(player, new String[]{});

        verify(player).sendMessage(contains("spawnsystem.reset.self"));
        verifyNoInteractions(persistentData);
    }

    @Test
    void execute_adminPermission_resetsAnotherPlayersSpawn() {
        Player player = playerWithPermission("spawnsystem.admin");
        UUID targetId = UUID.randomUUID();
        when(uuidChecker.findUUIDBasedOnPlayerName("Steve")).thenReturn(targetId);

        resetSpawnCommand.execute(player, new String[]{"Steve"});

        verify(persistentData).resetSpawn(targetId);
    }

    private Player playerWithPermission(String permission) {
        Player player = mock(Player.class);
        when(player.hasPermission(permission)).thenReturn(true);
        return player;
    }
}
