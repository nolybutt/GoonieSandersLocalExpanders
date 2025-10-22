# Repository Guidelines

## Project Structure & Module Organization
The NeoForge mod lives in `src/main/java/com/nolybutt/GoonieSandersLocalExpanders`, with the entrypoint `GoonieSandersLocalExpanders.java` and the ComputerCraft API surface in `LocalScriptApi.java`. Resources such as `neoforge.mods.toml`, the bundled helper (`assets/gooniesanderslocalexpanders/localscripts.lua`), the shell entry point (`assets/gooniesanderslocalexpanders/programs/goonie.lua`), and `pack.mcmeta` live under `src/main/resources`. Lua helper automation for in-game use ships from `in_game_script/`, while local ComputerCraft script payloads are stored (and generated at runtime) in `config/ccscripts/`. Keep new assets alongside their peers and avoid creating parallel package trees.

## Build, Test, and Development Commands
Use Gradle for all lifecycle tasks. Typical flows:
```
.\gradlew build         # compiles and bundles the NeoForge mod
.\gradlew runClient     # launches a dev client with the mod loaded
.\gradlew test          # executes any JVM-side unit tests
```
When adding new commands, document them here so other contributors stay aligned.

## Coding Style & Naming Conventions
Target Java 21 (configured via the Gradle toolchain). Prefer standard NeoForge/Java conventions: 4-space indentation, PascalCase for classes, camelCase for methods and fields, and snake_case only for Lua globals. Keep all log tags aligned with the mod id `gooniesanderslocalexpanders` (while the Lua global stays `GoonieSandersLocalExpanders`). If you add formatting automation, surface the configuration in this section.

## Testing Guidelines
Add JVM tests under `src/test/java` using JUnit 5 to cover API behavior and file handling. Lua workflows should be validated via minimal mocks placed in `in_game_script/` or in-world sanity runs. When a feature touches file IO, include at least one test that exercises missing/malformed script scenarios.

## Commit & Pull Request Guidelines
Craft commit messages in the form `feat: summary`, `fix: summary`, or `chore: summary` so history stays scannable. Pull requests should describe the mods touched, list validation steps (e.g., `.\gradlew build`), and attach screenshots or logs for in-game changes when useful. Reference related issues with `Closes #ID` and call out any follow-up work.

## Lua Script Workflow Notes
Ensure scripts designed for the API ship in `config/ccscripts` with `.lua` extensions. The mod automatically drops `localscripts.lua` into that folder, mounts it (plus the rest of the directory) inside computers at `/goonie`, and exposes the `goonie` command via a resource-mounted program; keep those behaviours intact when extending functionality.
