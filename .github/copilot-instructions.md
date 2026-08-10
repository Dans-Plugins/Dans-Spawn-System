# Copilot Instructions

This repository follows the DPC (Dans Plugins Community) conventions defined at
https://github.com/Dans-Plugins/dpc-conventions. Read those conventions before
making any changes.

## Technology Stack

- Language: Java
- Build tool: Maven
- Target platform: Spigot / Paper (Minecraft plugin, API version 1.13+)
- Test framework: JUnit 5 with Mockito, run via Maven Surefire (`mvn test`)

## Project Structure

- `src/main/java/dansplugins/spawnsystem/` – Plugin source code
  - `commands/` – Command executor classes
  - `listeners/` – Bukkit event listener classes
  - `services/` – Business-logic services (command dispatch, storage)
  - `utils/` – Utility helpers (block checking, UUID lookup, event registration)
  - `data/` – Persistent data model
  - `bstats/` – bStats metrics integration
- `src/main/resources/` – `plugin.yml` and other resources
- `src/test/java/` – Unit tests, mirroring the main source package layout

## Coding Conventions

- Follow the existing package structure (`dansplugins.spawnsystem.*`) when adding new classes.
- All user-facing messages are currently hard-coded strings; prefer extracting them to a lang file in `src/main/resources/lang/` for new contributions.
- Annotate every command executor and event listener with `@Override` where applicable.
- The plugin uses the Maven Shade plugin to produce a shaded JAR in `target/`.

## Contribution Workflow

- Branch from `develop` for all changes.
- Open a pull request against `develop`, not `main`.
- Reference the related GitHub issue in every pull request description.
