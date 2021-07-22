package dansplugins.spawnsystem;
import dansplugins.spawnsystem.commands.ResetSpawnCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandInterpreter {

    public boolean interpretCommand(CommandSender sender, String label, String[] args) {

        if (label.equalsIgnoreCase("resetspawn")) {
            ResetSpawnCommand command = new ResetSpawnCommand();
            command.execute(sender, args);
            return true;
        }

        return false;
    }

}
