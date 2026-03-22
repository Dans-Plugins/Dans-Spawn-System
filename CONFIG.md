# Configuration Guide

Dans-Spawn-System does not currently use a `config.yml` file. All configuration is performed through in-game signs and permissions.

## Permissions Configuration

Permissions can be managed through any standard Bukkit permissions plugin (e.g. LuckPerms).

| Permission Node | Default | Description |
|-----------------|---------|-------------|
| `spawnsystem.placeSpawnSign` | op | Allows placing `[Spawn]` signs |
| `spawnsystem.breakSpawnSign` | op | Allows breaking `[Spawn]` signs |
| `spawnsystem.reset.self` | op | Allows a player to reset their own spawn |
| `spawnsystem.reset.others` | op | Allows resetting another player's spawn |
| `spawnsystem.admin` | op | Grants all plugin permissions |

These defaults are defined in `src/main/resources/plugin.yml` and can be overridden by your permissions plugin.
