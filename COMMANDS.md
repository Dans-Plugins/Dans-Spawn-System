# Commands Reference

## Spawn Commands

### /resetspawn [player]

**Description:** Resets a player's custom spawn point, so they respawn as they would without the plugin (at their bed if they have one) and can select a new spawn from a `[Spawn]` sign.  
**Permission (no argument):** `spawnsystem.reset.self` or `spawnsystem.admin`  
**Permission (with argument):** `spawnsystem.reset.others` or `spawnsystem.admin`  
**Usage:** (in-game players only) `/resetspawn` or `/resetspawn <player>`  
**Example:** `/resetspawn` — resets your own spawn  
**Example:** `/resetspawn Steve` — resets Steve's spawn

The `<player>` name has to match the player's name exactly, including capitalisation (`/resetspawn steve` does not find `Steve`). The player can be offline, but must have joined the server before. A name that matches no one is reported as not found, and no spawn is reset.

When a spawn is reset, the sender is told `You have reset your spawn!` (no argument) or `Spawn reset for <player>!` (with an argument). The named player is told `Your spawn has been reset!` if they are online at the time; a player who is offline is not told. These messages are shown whether or not the player had a custom spawn to reset.

When nothing is reset, the sender is told why:

- from the console (or anything else that isn't an in-game player): `Sorry! This command can only be used by an in-game player.`
- without the permission: `Sorry! In order to use this command, you need the following permission: 'spawnsystem.reset.self'` (no argument) or `... 'spawnsystem.reset.others'` (with an argument). The message names only that node, but `spawnsystem.admin` is accepted in its place.
- with a name that matches no one: `Sorry! A player named '<player>' could not be found.`

Only the first argument is read; anything typed after the player name is ignored.
