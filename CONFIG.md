# Configuration Guide

Dans-Spawn-System creates a `config.yml` in `plugins/DansSpawnSystem/` on first run. Spawn behaviour
itself is configured through in-game signs and permissions; the file only holds the usage-reporting
settings below.

| Key | Default | Description |
|-----|---------|-------------|
| `usage-reporting.enabled` | `true` | Whether the plugin reports usage events (see below). Set to `false` to turn it off. |
| `usage-reporting.endpoint` | `https://trace.danielstephenson.dev` | The trace server events are sent to. |
| `usage-reporting.key` | the plugin's key | Identifies this plugin to the trace server so reports are attributed to it. Not a secret: it ships in the default config and can only report as DansSpawnSystem. Empty means reporting is off regardless of `enabled`. |

## Usage reporting

When the plugin is enabled, and each time one of its commands is used, a small event is sent to the
author's [trace](https://github.com/Stephenson-Software/trace-client-java) server so it is known which
plugins are actually in use. An event carries the plugin's name, the event name (`startup` or
`command`), and either the plugin version or the command name — nothing about players, the world, or
the server. Sending happens off the main thread, never delays a tick, and is dropped silently if the
server cannot be reached. Set `usage-reporting.enabled` to `false` to turn it off.

Servers upgraded from a version without a `config.yml` keep working: the plugin reads the bundled
defaults for any key the file lacks, so reporting is active there too unless it is turned off.

## Permissions Configuration

Permissions can be managed through any standard Bukkit permissions plugin (e.g. LuckPerms).

| Permission Node | Default | Description |
|-----------------|---------|-------------|
| `spawnsystem.placeSpawnSign` | op | Allows placing `[Spawn]` signs |
| `spawnsystem.breakSpawnSign` | op | Allows breaking `[Spawn]` signs |
| `spawnsystem.reset.self` | op | Allows a player to reset their own spawn |
| `spawnsystem.reset.others` | op | Allows resetting another player's spawn |
| `spawnsystem.admin` | op | Grants all plugin permissions |

By default, these permissions are granted to server operators (`op`) and can be overridden by your permissions plugin.
