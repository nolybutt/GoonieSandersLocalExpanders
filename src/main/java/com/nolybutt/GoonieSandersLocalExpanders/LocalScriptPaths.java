package com.nolybutt.GoonieSandersLocalExpanders;

import java.nio.file.Path;

final class LocalScriptPaths {
    static final Path SCRIPT_DIR = Path.of("config", "ccscripts");
    static final String DEFAULT_HELPER = "localscripts.lua";
    static final String MOUNT_PATH = "goonie";

    private LocalScriptPaths() {
    }
}
