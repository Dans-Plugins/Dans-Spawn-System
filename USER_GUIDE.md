# User Guide

## Prerequisites

- A Spigot or Paper Minecraft server (1.13 or later)
- The Dans-Spawn-System plugin installed in your `plugins/` folder

## First Steps

After installing the plugin and restarting your server, players can have their respawn location customised by interacting with spawn-selection signs placed by administrators.

## Common Scenarios

### Setting Up a Spawn Selection Sign (Admin)

1. Place a sign in the world.
2. On the **first line** of the sign, type `[Spawn]`.
3. On the **second line**, enter the X coordinate of the target spawn location.
4. On the **third line**, enter the Y coordinate.
5. On the **fourth line**, enter the Z coordinate.
6. Confirm placement — you will see a green confirmation message if you have the required permission.

**Example sign contents:**

```
[Spawn]
100
64
-200
```

Players who right-click this sign will have their respawn point set to coordinates (100, 64, -200) in their current world.

### Selecting a Spawn (Player)

Simply **right-click** any `[Spawn]` sign to set your personal respawn location to the coordinates written on that sign.

### Resetting a Player's Spawn (Admin)

Use the `/resetspawn` command to clear a player's custom spawn:

- Reset your own spawn: `/resetspawn`
- Reset another player's spawn: `/resetspawn <player>`

See [COMMANDS.md](COMMANDS.md) for full command details.

## Permissions

| Permission Node | Default | Description |
|-----------------|---------|-------------|
| `spawnsystem.placeSpawnSign` | op | Allows placing `[Spawn]` signs |
| `spawnsystem.breakSpawnSign` | op | Allows breaking `[Spawn]` signs |
| `spawnsystem.reset.self` | op | Allows resetting your own spawn with `/resetspawn` |
| `spawnsystem.reset.others` | op | Allows resetting another player's spawn with `/resetspawn <player>` |
| `spawnsystem.admin` | op | Grants all plugin permissions |
