package dansplugins.spawnsystem.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private static final String USAGE_REPORTING_SECTION = "usage-reporting";
    private static final String USAGE_REPORTING_ENABLED_KEY = "usage-reporting.enabled";
    private static final String USAGE_REPORTING_ENDPOINT_KEY = "usage-reporting.endpoint";
    private static final String USAGE_REPORTING_KEY_KEY = "usage-reporting.key";
    private static final String DEFAULT_USAGE_REPORTING_ENDPOINT = "https://trace.danielstephenson.dev";

    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void saveDefaultConfig() {
        plugin.saveDefaultConfig();
    }

    /**
     * Writes the usage-reporting block into config.yml when the file predates it, so the
     * opt-out is visible on disk on every server. saveDefaultConfig() never touches a
     * config.yml that already exists, so an upgraded installation would otherwise carry the
     * switch only in the jar. The values are the bundled defaults Bukkit registered for the
     * file, never new literals; a config.yml that already has the block is left alone.
     */
    public void saveUsageReportingDefaultsIfMissing() {
        FileConfiguration config = plugin.getConfig();
        if (config.isSet(USAGE_REPORTING_SECTION)) {
            return;
        }
        Configuration defaults = config.getDefaults();
        if (defaults == null || !defaults.isSet(USAGE_REPORTING_SECTION)) {
            return;
        }
        config.set(USAGE_REPORTING_ENABLED_KEY, defaults.get(USAGE_REPORTING_ENABLED_KEY));
        config.set(USAGE_REPORTING_ENDPOINT_KEY, defaults.get(USAGE_REPORTING_ENDPOINT_KEY));
        config.set(USAGE_REPORTING_KEY_KEY, defaults.get(USAGE_REPORTING_KEY_KEY));
        plugin.saveConfig();
    }

    // The one-argument getters, deliberately. Bukkit registers the jar's config.yml
    // as the defaults for the file on disk, and the one-argument getters fall
    // through to them -- but the two-argument getters return their explicit
    // fallback instead, which for the key would be "" and would turn reporting
    // off wherever the block is missing from disk (a hand-trimmed config.yml, or
    // a read-only plugins directory that saveUsageReportingDefaultsIfMissing()
    // could not write to). Verified against YamlConfiguration, not assumed.

    public boolean isUsageReportingEnabled() {
        return plugin.getConfig().getBoolean(USAGE_REPORTING_ENABLED_KEY);
    }

    public String getUsageReportingEndpoint() {
        String endpoint = plugin.getConfig().getString(USAGE_REPORTING_ENDPOINT_KEY);
        return endpoint != null ? endpoint : DEFAULT_USAGE_REPORTING_ENDPOINT;
    }

    /** Empty when no key is configured or bundled, which the client treats as "off". */
    public String getUsageReportingKey() {
        String key = plugin.getConfig().getString(USAGE_REPORTING_KEY_KEY);
        return key != null ? key : "";
    }
}
