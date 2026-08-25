package dansplugins.spawnsystem.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class PlayerDeathListenerTest {

    private final PlayerDeathListener playerDeathListener = new PlayerDeathListener();

    @Test
    void handle_anyDeath_keepsLevelsAndDropsNoExperience() {
        PlayerDeathEvent event =
                new PlayerDeathEvent(mock(Player.class), new ArrayList<ItemStack>(), 50, "died");

        playerDeathListener.handle(event);

        assertTrue(event.getKeepLevel());
        assertEquals(0, event.getDroppedExp());
    }
}
