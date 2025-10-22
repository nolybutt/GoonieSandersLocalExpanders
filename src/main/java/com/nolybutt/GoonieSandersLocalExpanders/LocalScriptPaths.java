package com.nolybutt.GoonieSandersLocalExpanders;

import java.nio.file.Path;

final class LocalScriptPaths {
    static final Path SCRIPT_DIR = Path.of("config", "ccscripts");
    static final String DEFAULT_HELPER = "localscripts.lua";
    static final String SCRIPT_MOUNT_PATH = "goonie";
    static final String PROGRAM_MOUNT_PATH = "rom/programs";
    static final String PROGRAM_RESOURCE_SUBPATH = "programs";

    private LocalScriptPaths() {
    }
}
