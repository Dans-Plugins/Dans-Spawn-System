package dansplugins.spawnsystem.services;

import dansplugins.spawnsystem.DansSpawnSystem;
import dansplugins.spawnsystem.commands.ResetSpawnCommand;
import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.utils.UUIDChecker;
import org.bukkit.command.CommandSender;

public class CommandService {
    private final PersistentData persistentData;
    private final UUIDChecker uuidChecker;
    private final DansSpawnSystem dansSpawnSystem;

    public CommandService(PersistentData persistentData, UUIDChecker uuidChecker, DansSpawnSystem dansSpawnSystem) {
        this.persistentData = persistentData;
        this.uuidChecker = uuidChecker;
        this.dansSpawnSystem = dansSpawnSystem;
    }

    public boolean interpretCommand(CommandSender sender, String label, String[] args) {

        if (label.equalsIgnoreCase("resetspawn")) {
            ResetSpawnCommand command = new ResetSpawnCommand(persistentData, uuidChecker, dansSpawnSystem);
            command.execute(sender, args);
            return true;
        }

        return false;
    }

}
