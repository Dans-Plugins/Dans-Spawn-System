package dansplugins.spawnsystem.services;

import dansplugins.spawnsystem.commands.ResetSpawnCommand;
import org.bukkit.command.CommandSender;

public class LocalCommandService {

    public boolean interpretCommand(CommandSender sender, String label, String[] args) {

        if (label.equalsIgnoreCase("resetspawn")) {
            ResetSpawnCommand command = new ResetSpawnCommand();
            command.execute(sender, args);
            return true;
        }

        return false;
    }

}
