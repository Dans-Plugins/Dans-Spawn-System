# Dans-Spawn-System

## Description

Dans-Spawn-System is a Minecraft plugin that allows players to use signs to select a custom spawn point in their world. Server operators place `[Spawn]` signs with coordinates, and players right-click those signs to set their personal respawn location.

## Installation

### First Time Installation

1. Download the plugin from [SpigotMC](https://www.spigotmc.org/resources/dans-spawn-system.95997/).
2. Place the jar in the `plugins` folder of your server.
3. Restart your server.

## Usage

### Documentation

- [User Guide](USER_GUIDE.md) – Getting started and common scenarios
- [Commands Reference](COMMANDS.md) – Complete list of all commands
- [Configuration Guide](CONFIG.md) – Detailed configuration options

### Wiki & Additional Resources

- [Wiki Guide](https://github.com/Dans-Plugins/Dans-Spawn-System/wiki)

## Support

You can find the support Discord server [here](https://discord.gg/xXtuAQ2).

### Experiencing a bug?

Please fill out a bug report [here](https://github.com/Dans-Plugins/Dans-Spawn-System/issues/new).

- [Known Bugs](https://github.com/Dans-Plugins/Dans-Spawn-System/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

## Contributing

- [CONTRIBUTING.md](CONTRIBUTING.md)
- [Notes for Developers](https://github.com/Dans-Plugins/Dans-Spawn-System/wiki)

## Testing

### Automated Tests

This project does not currently include automated unit tests.

To verify that the project builds successfully, run:

Linux:

    mvn clean package

Windows:

    mvn.cmd clean package

If you see `BUILD SUCCESS`, the project compiled successfully. For validating functional changes, follow the steps in the **Development** section below to run the plugin on a test server.

## Development

### Manual Testing on a Server

1. Build the plugin: `mvn clean package`
2. Copy the resulting jar from `target/` into your server's `plugins/` folder (exclude `original-*.jar`).
3. Start your test server and verify the plugin loads without errors.

## Authors and Acknowledgement

### Developers

| Name | Main Contributions |
|------|--------------------|
| DanTheTechMan | Original author and lead developer |

## License

This project is licensed under the [GNU General Public License v3.0](LICENSE) (GPL-3.0).

You are free to use, modify, and distribute this software, provided that:

- Source code is made available under the same license when distributed.
- Changes are documented and attributed.
- No additional restrictions are applied.

See the [LICENSE](LICENSE) file for the full text of the GPL-3.0 license.

## Project Status

This project is in active development.

### bStats

You can view the bStats page for the plugin [here](https://bstats.org/plugin/bukkit/DansSpawnSystem/12161).
