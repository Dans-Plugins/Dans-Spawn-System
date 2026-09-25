package dansplugins.spawnsystem.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UUIDCheckerTest {

    // UUIDChecker reads players through the static Bukkit accessors, and Bukkit.setServer can only be
    // called once per JVM, so a single mock server is installed here and its stubs are reset per test.
    private static final Server server = mock(Server.class);

    private final UUIDChecker uuidChecker = new UUIDChecker();

    @BeforeAll
    static void installServer() {
        if (Bukkit.getServer() == null) {
            when(server.getLogger()).thenReturn(Logger.getLogger(UUIDCheckerTest.class.getName()));
            Bukkit.setServer(server);
        }
    }

    @BeforeEach
    void clearPlayers() {
        reset(server);
        givenOnlinePlayers();
        givenOfflinePlayers();
    }

    @Test
    void findUUIDBasedOnPlayerName_onlinePlayer_returnsTheirUUID() {
        UUID id = UUID.randomUUID();
        givenOnlinePlayers(onlinePlayer("Steve", id));

        assertEquals(id, uuidChecker.findUUIDBasedOnPlayerName("Steve"));
    }

    @Test
    void findUUIDBasedOnPlayerName_onlinePlayer_isFoundWithoutConsultingOfflinePlayers() {
        givenOnlinePlayers(onlinePlayer("Steve", UUID.randomUUID()));

        uuidChecker.findUUIDBasedOnPlayerName("Steve");

        verify(server, never()).getOfflinePlayers();
    }

    @Test
    void findUUIDBasedOnPlayerName_offlinePlayer_returnsTheirUUID() {
        UUID id = UUID.randomUUID();
        givenOnlinePlayers(onlinePlayer("Alex", UUID.randomUUID()));
        givenOfflinePlayers(offlinePlayer("Steve", id));

        assertEquals(id, uuidChecker.findUUIDBasedOnPlayerName("Steve"));
    }

    @Test
    void findUUIDBasedOnPlayerName_offlinePlayerWithNoRecordedName_isSkipped() {
        UUID id = UUID.randomUUID();
        givenOfflinePlayers(offlinePlayer(null, UUID.randomUUID()), offlinePlayer("Steve", id));

        assertEquals(id, uuidChecker.findUUIDBasedOnPlayerName("Steve"));
    }

    @Test
    void findUUIDBasedOnPlayerName_onlineMatchTakesPrecedenceOverOfflineMatch() {
        UUID onlineId = UUID.randomUUID();
        givenOnlinePlayers(onlinePlayer("Steve", onlineId));
        givenOfflinePlayers(offlinePlayer("Steve", UUID.randomUUID()));

        assertEquals(onlineId, uuidChecker.findUUIDBasedOnPlayerName("Steve"));
    }

    @Test
    void findUUIDBasedOnPlayerName_nameDiffersOnlyInCase_isNotMatched() {
        givenOnlinePlayers(onlinePlayer("Steve", UUID.randomUUID()));
        givenOfflinePlayers(offlinePlayer("Steve", UUID.randomUUID()));

        assertNull(uuidChecker.findUUIDBasedOnPlayerName("steve"));
    }

    @Test
    void findUUIDBasedOnPlayerName_unknownName_returnsNull() {
        givenOnlinePlayers(onlinePlayer("Alex", UUID.randomUUID()));
        givenOfflinePlayers(offlinePlayer("Notch", UUID.randomUUID()));

        assertNull(uuidChecker.findUUIDBasedOnPlayerName("Ghost"));
    }

    private void givenOnlinePlayers(Player... players) {
        doReturn(Arrays.asList(players)).when(server).getOnlinePlayers();
    }

    private void givenOfflinePlayers(OfflinePlayer... players) {
        when(server.getOfflinePlayers()).thenReturn(players);
    }

    private Player onlinePlayer(String name, UUID id) {
        Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        when(player.getUniqueId()).thenReturn(id);
        return player;
    }

    private OfflinePlayer offlinePlayer(String name, UUID id) {
        OfflinePlayer player = mock(OfflinePlayer.class);
        when(player.getName()).thenReturn(name);
        when(player.getUniqueId()).thenReturn(id);
        return player;
    }
}
