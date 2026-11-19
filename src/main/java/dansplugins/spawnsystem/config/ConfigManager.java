package dansplugins.spawnsystem.config;

import dansplugins.spawnsystem.trace.UsageReportingSetting;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.function.Supplier;

public class ConfigManager {
    private static final String USAGE_REPORTING_SECTION = "usage-reporting";
    private static final String USAGE_REPORTING_ENABLED_KEY = "usage-reporting.enabled";
    private static final String USAGE_REPORTING_ENDPOINT_KEY = "usage-reporting.endpoint";
    private static final String USAGE_REPORTING_KEY_KEY = "usage-reporting.key";
    private static final String DEFAULT_USAGE_REPORTING_ENDPOINT = "https://trace.danielstephenson.dev";

    private final JavaPlugin plugin;
    private final Supplier<File> dataFolder;

    public ConfigManager(JavaPlugin plugin) {
        // A lambda: getDataFolder() is only set once the server has initialised the plugin.
        this(plugin, () -> plugin.getDataFolder());
    }

    /** @param dataFolder Supplies the folder config.yml is in (getDataFolder() is final, so tests pass their own). */
    ConfigManager(JavaPlugin plugin, Supplier<File> dataFolder) {
        this.plugin = plugin;
        this.dataFolder = dataFolder;
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
        // Bukkit loads a config.yml it cannot parse as empty, so the block looks missing even
        // when the operator wrote enabled: false in it. Writing the defaults here would replace
        // the whole file, opt-out included.
        if (UsageReportingSetting.isUnparseable(getConfigFile())) {
            return;
        }
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

    /**
     * Fails closed: only true, on or yes turns reporting on, so a quoted "false" or a typo
     * turns it off (getBoolean() would return the bundled true for them), and an opt-out in
     * config.yml on disk wins over the copy loaded at startup.
     */
    public boolean isUsageReportingEnabled() {
        return UsageReportingSetting.isEnabled(plugin.getConfig(), getConfigFile());
    }

    /** The plugin's config.yml, which may not exist yet. */
    public File getConfigFile() {
        return new File(dataFolder.get(), "config.yml");
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
