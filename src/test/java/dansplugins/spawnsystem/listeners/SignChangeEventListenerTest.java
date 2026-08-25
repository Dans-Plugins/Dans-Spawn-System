package dansplugins.spawnsystem.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SignChangeEventListenerTest {

    private final SignChangeEventListener signChangeEventListener = new SignChangeEventListener();

    @Test
    void handle_spawnSignWithPlacePermission_isConfirmedAndAllowed() {
        Player player = playerWithPermission("spawnsystem.placeSpawnSign");
        SignChangeEvent event = signWrittenBy(player, "[Spawn]");

        signChangeEventListener.handle(event);

        verify(player).sendMessage(ChatColor.GREEN + "Spawn selection sign created!");
        assertFalse(event.isCancelled());
    }

    @Test
    void handle_spawnSignWithAdminPermission_isConfirmedAndAllowed() {
        Player player = playerWithPermission("spawnsystem.admin");
        SignChangeEvent event = signWrittenBy(player, "[Spawn]");

        signChangeEventListener.handle(event);

        verify(player).sendMessage(ChatColor.GREEN + "Spawn selection sign created!");
        assertFalse(event.isCancelled());
    }

    @Test
    void handle_spawnSignWithoutPermission_isCancelledWithAnExplanation() {
        Player player = mock(Player.class);
        SignChangeEvent event = signWrittenBy(player, "[Spawn]");

        signChangeEventListener.handle(event);

        verify(player).sendMessage(contains("spawnsystem.placeSpawnSign"));
        assertTrue(event.isCancelled());
    }

    @Test
    void handle_signWithoutTheSpawnTag_isLeftAlone() {
        Player player = mock(Player.class);
        SignChangeEvent event = signWrittenBy(player, "Welcome!");

        signChangeEventListener.handle(event);

        verify(player, never()).sendMessage(anyString());
        assertFalse(event.isCancelled());
    }

    // The tag is matched with contains() rather than equals(), so surrounding text on the first line
    // still produces a spawn selection sign.
    @Test
    void handle_spawnTagSurroundedByOtherText_isStillTreatedAsASpawnSign() {
        Player player = playerWithPermission("spawnsystem.placeSpawnSign");
        SignChangeEvent event = signWrittenBy(player, "The [Spawn] sign");

        signChangeEventListener.handle(event);

        verify(player).sendMessage(ChatColor.GREEN + "Spawn selection sign created!");
    }

    private SignChangeEvent signWrittenBy(Player player, String firstLine) {
        return new SignChangeEvent(mock(Block.class), player, new String[]{firstLine, "", "", ""});
    }

    private Player playerWithPermission(String permission) {
        Player player = mock(Player.class);
        when(player.hasPermission(permission)).thenReturn(true);
        return player;
    }
}
