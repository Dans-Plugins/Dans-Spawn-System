# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Added

- A `Dev Release` workflow, which republishes a rolling `dev` prerelease of `master` on every non-documentation push. This is what Dan's Plugin Manager's experimental channel installs from: `/dpm get dansspawnsystem --experimental` reads `releases/tags/dev`, so without it there is nothing for that command to download. The prerelease is unreleased, unreviewed code and is marked as such.

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
