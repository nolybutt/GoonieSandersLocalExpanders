package com.nolybutt.GoonieSandersLocalExpanders;

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

    public LocalScriptApi(IComputerSystem computer) {
        this.computer = computer;
        try {
            Files.createDirectories(LocalScriptPaths.SCRIPT_DIR);
        } catch (IOException e) {
            System.err.println("[GoonieSandersLocalExpanders] Could not create scripts directory: " + e.getMessage());
        }
    }

    @Override
    public String[] getNames() {
        return new String[]{"GoonieSandersLocalExpanders"};
    }

    @Override
    public void startup() {
        try {
            computer.mountWritable(LocalScriptPaths.MOUNT_PATH, new LocalScriptMount(LocalScriptPaths.SCRIPT_DIR));
        } catch (IOException e) {
            System.err.println("[GoonieSandersLocalExpanders] Failed to mount local scripts: " + e.getMessage());
        }
    }

    @Override
    public void shutdown() {
        computer.unmount(LocalScriptPaths.MOUNT_PATH);
    }

    @LuaFunction(mainThread = true)
    public final List<String> listScripts() throws LuaException {
        try (Stream<Path> stream = Files.list(LocalScriptPaths.SCRIPT_DIR)) {
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

        var resolved = LocalScriptPaths.SCRIPT_DIR.resolve(name).normalize();
        if (!resolved.startsWith(LocalScriptPaths.SCRIPT_DIR)) throw new LuaException("Invalid script path");
        return resolved;
    }
}
