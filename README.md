![Demo](berniesanders.gif)

# Goonie Sanders Local Expanders

Brothers, sisters, and assorted goons: for far too long the working computers of ComputerCraft have been denied unfettered access to the scripts they already wrote on their home machines. I am once again asking you to load your Lua from the local filesystem—only this time, I brought a mod to make it painless.

## What This Wild Thing Does
- **Expands the local script commons** by exposing `config/ccscripts/` through a global API named `GoonieSandersLocalExpanders`.
- **Mounts the whole folder in-game** under `/goonie`, so you can run or copy scripts straight from your dev machine without typing them in by hand.
- **Ships a campaign aide** (`localscripts.lua`, auto-dropped into `config/ccscripts/`) that walks you through picking a script, saves it to `/scripts/`, and—if you say “aye”—executes it immediately.

## Installation (Because revolutions need logistics)
1. Build or grab the mod jar (`.\gradlew build` if you’re the DIY type).
2. Toss the jar into your Forge mods folder.
3. Drop any Lua masterpieces into `config/ccscripts/`. That directory is the beating heart of the operation.

## How To Use It On The Floor
1. Plop down a ComputerCraft computer or turtle.
2. Run `goonie` (auto-installed under `/rom/programs`) to launch the helper immediately.
3. Choose from the list, let the API fetch it, and watch the scripts seize the means of execution.

Want to skip the helper? Access everything manually with plain shell commands like `fs.list("/goonie")`, `cp /goonie/myScript.lua myScript.lua`, or even `shell.run("/goonie/myScript.lua")`—they all hit the same mounted folder the helper uses.

## F.A.Q. (Frequently Agitated Questions)
- **Does this require network access?** Absolutely not. We’re keeping it local, comrades.
- **What if a script is missing?** The mod will let you know. Place it in `config/ccscripts/` and rerun.
- **Can I contribute?** Yes. Read `AGENTS.md`, follow the guidelines, and remember: every pull request is a step toward universal turtle literacy.

Now get out there, organize your Lua, and expand locally like the gooniest Bernie you can be. The revolution may not be televised, but it will be scripted.
