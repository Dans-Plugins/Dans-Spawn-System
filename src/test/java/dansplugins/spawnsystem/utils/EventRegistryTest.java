package dansplugins.spawnsystem.utils;

import dansplugins.spawnsystem.data.PersistentData;
import dansplugins.spawnsystem.listeners.BlockBreakListener;
import dansplugins.spawnsystem.listeners.PlayerDeathListener;
import dansplugins.spawnsystem.listeners.PlayerInteractListener;
import dansplugins.spawnsystem.listeners.PlayerRespawnListener;
import dansplugins.spawnsystem.listeners.SignChangeEventListener;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventRegistryTest {

    private final Plugin plugin = mock(Plugin.class);
    private final Server server = mock(Server.class);
    private final PluginManager pluginManager = mock(PluginManager.class);
    private final EventRegistry eventRegistry =
            new EventRegistry(plugin, mock(BlockChecker.class), mock(PersistentData.class));

    @BeforeEach
    void wirePluginManagerToPlugin() {
        when(plugin.getServer()).thenReturn(server);
        when(server.getPluginManager()).thenReturn(pluginManager);
    }

    // A listener left out of registration fails silently in-game - its event is simply never handled - so the
    // full set is pinned here rather than relying on a manual server check to notice the gap.
    @Test
    void registerEvents_registersEachListenerOnce() {
        eventRegistry.registerEvents();

        ArgumentCaptor<Listener> listeners = ArgumentCaptor.forClass(Listener.class);
        verify(pluginManager, times(5)).registerEvents(listeners.capture(), any(Plugin.class));
        // Each listener handles a different event type, so the order they are registered in is not asserted.
        Set<Class<?>> registered = listeners.getAllValues().stream()
                .map(Object::getClass)
                .collect(Collectors.toSet());
        assertEquals(new HashSet<>(Arrays.asList(
                BlockBreakListener.class,
                PlayerDeathListener.class,
                PlayerInteractListener.class,
                PlayerRespawnListener.class,
                SignChangeEventListener.class
        )), registered);
    }

    @Test
    void registerEvents_registersEveryListenerAgainstTheGivenPlugin() {
        eventRegistry.registerEvents();

        verify(pluginManager, times(5)).registerEvents(any(Listener.class), eq(plugin));
    }
}
