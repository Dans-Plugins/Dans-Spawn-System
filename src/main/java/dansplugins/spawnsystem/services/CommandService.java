package dansplugins.spawnsystem.services;

import dansplugins.spawnsystem.commands.ResetSpawnCommand;
import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.utils.UUIDChecker;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandService {
    private final PersistentData persistentData;
    private final UUIDChecker uuidChecker;
    private final Server server;

    public CommandService(PersistentData persistentData, UUIDChecker uuidChecker, Server server) {
        this.persistentData = persistentData;
        this.uuidChecker = uuidChecker;
        this.server = server;
    }

    // Routes on the command's registered name rather than the label it was typed as, so that the
    // namespaced form (/dansspawnsystem:resetspawn) reaches the same command as /resetspawn.
    public boolean interpretCommand(CommandSender sender, Command command, String[] args) {

        if (command.getName().equalsIgnoreCase("resetspawn")) {
            ResetSpawnCommand resetSpawnCommand = new ResetSpawnCommand(persistentData, uuidChecker, server);
            resetSpawnCommand.execute(sender, args);
            return true;
        }

        return false;
    }

}
