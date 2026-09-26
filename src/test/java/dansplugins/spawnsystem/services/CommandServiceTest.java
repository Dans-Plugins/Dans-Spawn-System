package dansplugins.spawnsystem.services;

import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.utils.UUIDChecker;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CommandServiceTest {

    private final PersistentData persistentData = mock(PersistentData.class);
    private final UUIDChecker uuidChecker = mock(UUIDChecker.class);
    private final Server server = mock(Server.class);
    private final CommandService commandService = new CommandService(persistentData, uuidChecker, server);

    @Test
    void interpretCommand_resetspawn_isHandledByResetSpawnCommand() {
        CommandSender console = mock(CommandSender.class);

        boolean handled = commandService.interpretCommand(console, commandNamed("resetspawn"), new String[]{});

        assertTrue(handled);
        verify(console).sendMessage(ChatColor.RED + "Sorry! This command can only be used by an in-game player.");
    }

    @Test
    void interpretCommand_routesOnTheCommandNameNotTheTypedLabel() {
        // Bukkit passes the label as typed, so /dansspawnsystem:resetspawn arrives with that label while the
        // command's name is still "resetspawn"; the name is all this service is given to route on.
        CommandSender console = mock(CommandSender.class);
        Command command = commandNamed("resetspawn");
        when(command.getLabel()).thenReturn("dansspawnsystem:resetspawn");

        boolean handled = commandService.interpretCommand(console, command, new String[]{});

        assertTrue(handled);
        verify(console).sendMessage(ChatColor.RED + "Sorry! This command can only be used by an in-game player.");
    }

    @Test
    void interpretCommand_nameIsMatchedCaseInsensitively() {
        CommandSender console = mock(CommandSender.class);

        boolean handled = commandService.interpretCommand(console, commandNamed("ResetSpawn"), new String[]{});

        assertTrue(handled);
    }

    @Test
    void interpretCommand_unknownCommand_isNotHandled() {
        CommandSender sender = mock(CommandSender.class);

        boolean handled = commandService.interpretCommand(sender, commandNamed("setspawn"), new String[]{});

        assertFalse(handled);
        verifyNoInteractions(sender);
        verifyNoInteractions(persistentData);
    }

    private Command commandNamed(String name) {
        Command command = mock(Command.class);
        when(command.getName()).thenReturn(name);
        return command;
    }
}
