package com.nolybutt.GoonieSandersLocalExpanders;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalScriptPeripheral implements IPeripheral {
    private final Path scriptDir = Path.of("config/ccscripts");

    public LocalScriptPeripheral() {
        try {
            Files.createDirectories(scriptDir);
        } catch (IOException e) {
            System.err.println("[GoonieSandersLocalExpanders] Could not create scripts directory: " + e.getMessage());
        }
    }

    @Override
    public String getType() {
        return "GoonieSandersLocalExpanders";
    }

    @Override
    public String[] getMethodNames() {
        return new String[]{"listScripts", "getScript"};
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] args)
            throws LuaException, InterruptedException {

        switch (method) {
            case 0 -> { // listScripts
                try (Stream<Path> stream = Files.list(scriptDir)) {
                    List<String> files = stream
                            .filter(p -> p.toString().endsWith(".lua"))
                            .map(p -> p.getFileName().toString())
                            .collect(Collectors.toList());
                    return new Object[]{files};
                } catch (IOException e) {
                    throw new LuaException("Error listing scripts: " + e.getMessage());
                }
            }

            case 1 -> { // getScript(name)
                if (args.length == 0) throw new LuaException("Usage: getScript(name)");
                Path file = scriptDir.resolve(args[0].toString());
                if (!Files.exists(file)) throw new LuaException("No such file: " + file);
                try {
                    String content = Files.readString(file);
                    return new Object[]{content};
                } catch (IOException e) {
                    throw new LuaException("Error reading file: " + e.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(IPeripheral other) {
        return other instanceof LocalScriptPeripheral;
    }
}

