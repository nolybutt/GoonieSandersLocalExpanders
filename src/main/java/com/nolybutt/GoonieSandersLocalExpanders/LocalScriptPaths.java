package com.nolybutt.GoonieSandersLocalExpanders;

import java.nio.file.Path;

final class LocalScriptPaths {
    static final String SCRIPT_DIR_PROPERTY = "gooniesanderslocalexpanders.scriptDir";
    static final Path SCRIPT_DIR = Path.of("config", "ccscripts");
    static final String DEFAULT_HELPER = "localscripts.lua";
    static final String SCRIPT_MOUNT_PATH = "goonie";
    static final String PROGRAM_MOUNT_PATH = "rom/programs";
    static final String PROGRAM_RESOURCE_SUBPATH = "programs";

    static Path getScriptDir() {
        String override = System.getProperty(SCRIPT_DIR_PROPERTY);
        if (override == null || override.isBlank()) return SCRIPT_DIR;
        return Path.of(override);
    }

    private LocalScriptPaths() {
    }
}
