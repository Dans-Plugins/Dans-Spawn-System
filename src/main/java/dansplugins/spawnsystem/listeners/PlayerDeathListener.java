package dansplugins.spawnsystem.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler()
    public void handle(PlayerDeathEvent event) {
        event.setKeepLevel(true);
        event.setDroppedExp(0);
    }

}
