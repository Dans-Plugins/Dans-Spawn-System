package dansplugins.spawnsystem.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfigManagerTest {
    private JavaPlugin plugin;
    private FileConfiguration config;
    private ConfigManager configManager;

    @BeforeEach
    void setUp() {
        plugin = mock(JavaPlugin.class);
        config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        configManager = new ConfigManager(plugin);
    }

    @Test
    void saveDefaultConfig_delegatesToPlugin() {
        configManager.saveDefaultConfig();

        verify(plugin).saveDefaultConfig();
    }

    @Test
    void usageReporting_readsThroughToTheBundledDefaultsWhenTheFileHasNoBlock() {
        // A server upgraded from before usage reporting has no usage-reporting
        // block in its config.yml (this plugin had no config.yml at all before).
        // Bukkit's one-argument getters fall through to the jar's defaults; the
        // two-argument ones would return their fallback and turn reporting off
        // on every existing installation.
        when(config.getBoolean("usage-reporting.enabled")).thenReturn(true);
        when(config.getString("usage-reporting.endpoint")).thenReturn("https://trace.danielstephenson.dev");
        when(config.getString("usage-reporting.key")).thenReturn("bundled-key");

        assertTrue(configManager.isUsageReportingEnabled());
        assertEquals("https://trace.danielstephenson.dev", configManager.getUsageReportingEndpoint());
        assertEquals("bundled-key", configManager.getUsageReportingKey());
        verify(config, never()).getString(eq("usage-reporting.key"), anyString());
        verify(config, never()).getString(eq("usage-reporting.endpoint"), anyString());
        verify(config, never()).getBoolean(eq("usage-reporting.enabled"), anyBoolean());
    }

    @Test
    void usageReporting_isOffWithNoKeyAnywhere() {
        when(config.getString("usage-reporting.key")).thenReturn(null);
        when(config.getString("usage-reporting.endpoint")).thenReturn(null);

        assertEquals("", configManager.getUsageReportingKey(), "no key anywhere must read as off, not as null");
        assertEquals("https://trace.danielstephenson.dev", configManager.getUsageReportingEndpoint());
    }

    @Test
    void usageReporting_readsTheConfiguredValues() {
        when(config.getBoolean("usage-reporting.enabled")).thenReturn(false);
        when(config.getString("usage-reporting.endpoint")).thenReturn("http://localhost:8080");
        when(config.getString("usage-reporting.key")).thenReturn("abc");

        assertFalse(configManager.isUsageReportingEnabled());
        assertEquals("http://localhost:8080", configManager.getUsageReportingEndpoint());
        assertEquals("abc", configManager.getUsageReportingKey());
    }

    // The block-on-disk write. Exercised against a real YamlConfiguration with the
    // jar's config.yml registered as its defaults, exactly as JavaPlugin.reloadConfig()
    // sets it up, so the isSet()/getDefaults() semantics are measured, not assumed.

    private static YamlConfiguration bundledDefaults() {
        YamlConfiguration defaults = new YamlConfiguration();
        defaults.set("usage-reporting.enabled", true);
        defaults.set("usage-reporting.endpoint", "https://trace.danielstephenson.dev");
        defaults.set("usage-reporting.key", "bundled-key");
        return defaults;
    }

    @Test
    void saveUsageReportingDefaultsIfMissing_writesTheBundledBlockWhenTheFileHasNone() {
        YamlConfiguration onDisk = new YamlConfiguration();
        onDisk.setDefaults(bundledDefaults());
        when(plugin.getConfig()).thenReturn(onDisk);
        assertFalse(onDisk.isSet("usage-reporting"), "defaults alone must not count as the block being on disk");

        configManager.saveUsageReportingDefaultsIfMissing();

        assertTrue(onDisk.isSet("usage-reporting"));
        assertEquals(true, onDisk.get("usage-reporting.enabled", null));
        assertEquals("https://trace.danielstephenson.dev", onDisk.get("usage-reporting.endpoint", null));
        assertEquals("bundled-key", onDisk.get("usage-reporting.key", null));
        verify(plugin).saveConfig();
    }

    @Test
    void saveUsageReportingDefaultsIfMissing_leavesAnExistingBlockAlone() {
        YamlConfiguration onDisk = new YamlConfiguration();
        onDisk.set("usage-reporting.enabled", false);
        onDisk.setDefaults(bundledDefaults());
        when(plugin.getConfig()).thenReturn(onDisk);

        configManager.saveUsageReportingDefaultsIfMissing();

        assertFalse(onDisk.getBoolean("usage-reporting.enabled"), "an operator's opt-out must survive");
        assertNull(onDisk.get("usage-reporting.key", null), "nothing is added to a file that has the block");
        verify(plugin, never()).saveConfig();
    }

    @Test
    void saveUsageReportingDefaultsIfMissing_doesNothingWithoutBundledDefaults() {
        YamlConfiguration onDisk = new YamlConfiguration();
        when(plugin.getConfig()).thenReturn(onDisk);

        configManager.saveUsageReportingDefaultsIfMissing();

        assertFalse(onDisk.isSet("usage-reporting"));
        verify(plugin, never()).saveConfig();
    }
}
