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
