# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Fixed
- Players could set their spawn more than once because the duplicate-spawn check looked up the player by name in a map keyed by UUID, so the check never matched. The check now uses the player's UUID, consistent with how spawns are stored.

## [1.2] – (date unknown)

### Changed
- Internal refactoring of spawn storage and event handling.

## [1.0] – (date unknown)

### Added
- Initial release.
- `[Spawn]` sign mechanic: right-clicking a sign sets the player's respawn point.
- `/resetspawn` command to clear a player's custom spawn.
- bStats metrics integration (plugin ID 12161).
