package com.nolybutt.GoonieSandersLocalExpanders;

import dan200.computercraft.api.lua.LuaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalScriptApiTest {
    @TempDir
    Path tempDir;

    @AfterEach
    void clearOverride() {
        System.clearProperty(LocalScriptPaths.SCRIPT_DIR_PROPERTY);
    }

    @Test
    void listScriptsReturnsOnlyLuaSorted() throws IOException, LuaException {
        System.setProperty(LocalScriptPaths.SCRIPT_DIR_PROPERTY, tempDir.toString());
        Files.writeString(tempDir.resolve("b.lua"), "print('b')");
        Files.writeString(tempDir.resolve("a.lua"), "print('a')");
        Files.writeString(tempDir.resolve("not_lua.txt"), "nope");

        var api = new LocalScriptApi(null);
        List<String> scripts = api.listScripts();

        assertEquals(List.of("a.lua", "b.lua"), scripts);
    }

    @Test
    void getScriptReadsFileContents() throws IOException, LuaException {
        System.setProperty(LocalScriptPaths.SCRIPT_DIR_PROPERTY, tempDir.toString());
        Files.writeString(tempDir.resolve("hello.lua"), "print('hello')");

        var api = new LocalScriptApi(null);
        assertEquals("print('hello')", api.getScript("hello.lua"));
    }

    @Test
    void getScriptRejectsTraversalOutsideRoot() {
        System.setProperty(LocalScriptPaths.SCRIPT_DIR_PROPERTY, tempDir.toString());

        var api = new LocalScriptApi(null);
        LuaException exception = assertThrows(LuaException.class, () -> api.getScript("../escape.lua"));
        assertTrue(exception.getMessage().contains("Invalid script path"));
    }

    @Test
    void getScriptReportsMissingFile() {
        System.setProperty(LocalScriptPaths.SCRIPT_DIR_PROPERTY, tempDir.toString());

        var api = new LocalScriptApi(null);
        LuaException exception = assertThrows(LuaException.class, () -> api.getScript("missing.lua"));
        assertTrue(exception.getMessage().contains("No such file"));
    }

    @Test
    void getScriptReportsUnreadableEntry() throws IOException {
        System.setProperty(LocalScriptPaths.SCRIPT_DIR_PROPERTY, tempDir.toString());
        Files.createDirectories(tempDir.resolve("broken.lua"));

        var api = new LocalScriptApi(null);
        LuaException exception = assertThrows(LuaException.class, () -> api.getScript("broken.lua"));
        assertTrue(exception.getMessage().contains("Error reading file"));
    }
}

