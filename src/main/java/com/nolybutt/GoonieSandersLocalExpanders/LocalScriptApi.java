package com.nolybutt.GoonieSandersLocalExpanders;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.IComputerSystem;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Lua API exposed to every ComputerCraft machine that surfaces scripts stored under {@code config/ccscripts}.
 */
public class LocalScriptApi implements ILuaAPI {
    private final IComputerSystem computer;
    private String scriptMountId;
    private String programMountId;

    public LocalScriptApi(IComputerSystem computer) {
        this.computer = computer;
        try {
            Files.createDirectories(LocalScriptPaths.getScriptDir());
        } catch (IOException e) {
            GoonieSandersLocalExpanders.LOGGER.error(
                "[{}] Could not create scripts directory",
                GoonieSandersLocalExpanders.MOD_ID,
                e
            );
        }
    }

    @Override
    public String[] getNames() {
        return new String[]{"GoonieSandersLocalExpanders"};
    }

    @Override
    public void startup() {
        try {
            scriptMountId = computer.mountWritable(
                LocalScriptPaths.SCRIPT_MOUNT_PATH,
                new LocalScriptMount(LocalScriptPaths.getScriptDir())
            );
        } catch (IOException e) {
            GoonieSandersLocalExpanders.LOGGER.error(
                "[{}] Failed to mount local scripts",
                GoonieSandersLocalExpanders.MOD_ID,
                e
            );
        }

        var server = computer.getLevel().getServer();
        if (server != null) {
            var programMount = ComputerCraftAPI.createResourceMount(
                server,
                GoonieSandersLocalExpanders.MOD_ID,
                LocalScriptPaths.PROGRAM_RESOURCE_SUBPATH
            );
            if (programMount != null) {
                programMountId = computer.mount(LocalScriptPaths.PROGRAM_MOUNT_PATH, programMount);
            }
        }
    }

    @Override
    public void shutdown() {
        if (scriptMountId != null) {
            computer.unmount(scriptMountId);
            scriptMountId = null;
        }
        if (programMountId != null) {
            computer.unmount(programMountId);
            programMountId = null;
        }
    }

    @LuaFunction(mainThread = true)
    public final List<String> listScripts() throws LuaException {
        try (Stream<Path> stream = Files.list(LocalScriptPaths.getScriptDir())) {
            return stream
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(name -> name.endsWith(".lua"))
                .sorted()
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new LuaException("Error listing scripts: " + e.getMessage());
        }
    }

    @LuaFunction(mainThread = true)
    public final String getScript(String name) throws LuaException {
        var scriptPath = normaliseScriptPath(name);
        if (!Files.exists(scriptPath)) throw new LuaException("No such file: " + name);

        try {
            return Files.readString(scriptPath);
        } catch (IOException e) {
            throw new LuaException("Error reading file: " + e.getMessage());
        }
    }

    private Path normaliseScriptPath(String name) throws LuaException {
        if (name == null || name.isBlank()) throw new LuaException("Usage: getScript(name)");

        var root = LocalScriptPaths.getScriptDir().toAbsolutePath().normalize();
        var resolved = root.resolve(name).normalize();
        if (!resolved.startsWith(root)) throw new LuaException("Invalid script path");
        return resolved;
    }
}
