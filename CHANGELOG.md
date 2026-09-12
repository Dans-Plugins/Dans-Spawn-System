# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Fixed

- A `[Spawn]` sign set a player's spawn when it was left-clicked, not only when it was right-clicked, so a player who started to break one — or who left-clicked one while holding a tool — had their spawn set permanently and needed an operator to run `/resetspawn <player>` to undo it. `USER_GUIDE.md` has always documented right-clicking as the way a spawn is selected, and only a right-click on the sign now does so.
- Clicking a `[Spawn]` sign whose coordinates could not be read as whole numbers told the player nothing at all, leaving a malformed sign indistinguishable from a decorative one. The player is now told that the coordinates could not be read. The accompanying console line moves from standard output to the plugin's own logger, so it is attributed to this plugin and now carries the underlying parse failure instead of discarding it.
- The `Dev Release` workflow now retries publishing the `dev` prerelease before giving up. The release and its tag have to be deleted and recreated for the tag to move to the new commit, and a transient API failure inside that window previously left the repository with no `dev` release at all until the workflow was re-run by hand. Each attempt now starts from a clean slate, and an exhausted retry fails loudly.

### Added

- The plugin now reports usage events — `startup` on enable, `command` on each of its commands — to the author's trace server so it is known which plugins are in use. Events carry the plugin name, the event name, and the plugin version or command name; nothing about players or the server. Reporting runs off the main thread, never delays a tick, drops silently when the server is unreachable, and is turned off with `usage-reporting.enabled: false` in `config.yml`. The default config carries the plugin's key, so reporting is active out of the box unless turned off — including on servers upgraded from a version before the `usage-reporting` block existed, whose `config.yml` is never rewritten: the plugin reads the bundled defaults for any key the file lacks
- A `config.yml`, created in `plugins/DansSpawnSystem/` on first run. It currently holds only the `usage-reporting` block; spawn behaviour is still configured through signs and permissions
- A `Dev Release` workflow, which republishes a rolling `dev` prerelease of `master` on every non-documentation push. This is what Dan's Plugin Manager's experimental channel installs from: `/dpm get dansspawnsystem --experimental` reads `releases/tags/dev`, so without it there is nothing for that command to download. The prerelease is unreleased, unreviewed code and is marked as such.
- Unit tests for `ResetSpawnCommand` covering permission handling, unresolvable names, offline and online targets, and non-player senders.
- Unit tests for every class in the `listeners` package, which previously had none: placing and breaking `[Spawn]` signs with and without permission, the protection of blocks adjacent to a spawn sign, setting a spawn by clicking a sign (including unreadable and decimal coordinates), experience and level handling on death, the respawn paths that defer to vanilla behaviour, and the one-tick-deferred teleport to a custom spawn together with the message that accompanies it.

### Fixed
- `[Spawn]` signs made from wood types added after Minecraft 1.15 — crimson, warped, mangrove, bamboo and cherry, along with every hanging sign — were not recognised as signs, so they did nothing when right-clicked and were not protected from being broken without `spawnsystem.breakSpawnSign`. Sign detection is now based on the material name, so those types work and future ones are picked up without a rebuild.
- A saved spawn whose X, Y or Z coordinate was exactly `0` was discarded on load, so a spawn near the world origin was lost on every restart. Whether a coordinate was present in the file is now tracked directly instead of being inferred from the value, and a coordinate that cannot be read as a number is reported with the offending text.
- `/resetspawn <player>` reported a green success message even when the supplied name matched no known player, so an operator was told a reset had happened when nothing was changed. An unresolvable name is now reported as such and no reset is claimed.
- `/resetspawn` produced no output at all when run from the console, a command block, or RCON. Non-player senders are now told that the command is for in-game players only.
- A player targeted by `/resetspawn <player>` while offline no longer causes a swallowed `NullPointerException`; the target is looked up by UUID and notified only when actually online.

### Changed
- `ResetSpawnCommand` now depends on `org.bukkit.Server` rather than on the plugin instance, which allows its behaviour to be unit tested.
- `PlayerRespawnListener` now depends on `org.bukkit.plugin.Plugin` rather than on the final `DansSpawnSystem` class, so the scheduled teleport to a custom spawn can be unit tested. Behaviour is unchanged.

## [2.0.0-SNAPSHOT-8-8-2026] – 2026-08-08

### Changed
- Dans-Spawn-System is now developed AI-first. Day-to-day feature work, grooming, review and maintenance run through AI agents working directly against this repository, with the maintainers setting direction and approving what lands. The major version bump marks that change in how the project is built — it is not a break in behaviour, configuration or stored data, and existing installations can upgrade in place. Released as `2.0.0-SNAPSHOT-8-8-2026`: the AI-first line has not yet been verified in live operation, and the dated snapshot designation stays until it has.

### Fixed
- Players could set their spawn more than once because the duplicate-spawn check looked up the player by name in a map keyed by UUID, so the check never matched. The check now uses the player's UUID, consistent with how spawns are stored.
- The `Build` CI workflow only ran on pushes/PRs targeting `main`/`develop`, but the repository's default branch is `master`, so it never ran. It now targets `master`.

## [1.2] – (date unknown)

### Changed
- Internal refactoring of spawn storage and event handling.

## [1.0] – (date unknown)

### Added
- Initial release.
- `[Spawn]` sign mechanic: right-clicking a sign sets the player's respawn point.
- `/resetspawn` command to clear a player's custom spawn.
- bStats metrics integration (plugin ID 12161).
