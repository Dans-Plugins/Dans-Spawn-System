package dansplugins.spawnsystem.eventhandlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathEventHandler implements Listener {

    @EventHandler()
    public void handle(PlayerDeathEvent event) {
        event.setKeepLevel(true);
        event.setDroppedExp(0);
    }

}
