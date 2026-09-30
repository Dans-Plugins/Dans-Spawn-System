# User Guide

## Prerequisites

- A Spigot or Paper Minecraft server, 1.19.4 or later (see [Supported Minecraft Versions](README.md#supported-minecraft-versions) for the versions each release is tested on)
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

Simply **right-click** any `[Spawn]` sign to set your personal respawn location to the coordinates written on that sign. You are teleported to those coordinates straight away.

A spawn can only be selected once. Right-clicking another `[Spawn]` sign afterwards tells you that you have already set your spawn; it has to be reset with `/resetspawn` (by you, if you have `spawnsystem.reset.self`, or by an admin) before you can choose again.

### Resetting a Player's Spawn (Admin)

Use the `/resetspawn` command to clear a player's custom spawn:

- Reset your own spawn: `/resetspawn`
- Reset another player's spawn: `/resetspawn <player>`

Once reset, the player respawns as they would without the plugin, and can select a spawn again by right-clicking a `[Spawn]` sign.

See [COMMANDS.md](COMMANDS.md) for full command details.

## How Respawning Works

- **Custom spawn:** a player with a custom spawn is teleported to it just after respawning, with the message "Teleporting to custom spawn!".
- **Beds take precedence:** a player who has a bed spawn respawns at the bed as normal; their custom spawn is kept but not used while the bed spawn exists.
- **Protected signs:** breaking a `[Spawn]` sign, or any block directly touching one (above, below, or on any side), requires `spawnsystem.breakSpawnSign`.
- **Experience on death:** while the plugin is installed, every player on the server keeps their experience level when they die and drops no experience orbs, whether or not they have a custom spawn.

## Permissions

| Permission Node | Default | Description |
|-----------------|---------|-------------|
| `spawnsystem.placeSpawnSign` | op | Allows placing `[Spawn]` signs |
| `spawnsystem.breakSpawnSign` | op | Allows breaking `[Spawn]` signs and the blocks directly touching them |
| `spawnsystem.reset.self` | op | Allows resetting your own spawn with `/resetspawn` |
| `spawnsystem.reset.others` | op | Allows resetting another player's spawn with `/resetspawn <player>` |
| `spawnsystem.admin` | op | Grants all plugin permissions |
