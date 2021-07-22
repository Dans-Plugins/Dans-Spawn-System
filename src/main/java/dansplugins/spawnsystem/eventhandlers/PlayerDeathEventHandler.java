package dansplugins.spawnsystem.eventhandlers;

import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathEventHandler {

    public void handle(PlayerDeathEvent event) {
        event.setKeepLevel(true);
        event.setDroppedExp(0);
    }

}
