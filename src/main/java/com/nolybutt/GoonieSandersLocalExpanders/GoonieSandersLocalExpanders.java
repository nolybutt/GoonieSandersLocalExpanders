package com.nolybutt.GoonieSandersLocalExpanders;

import dan200.computercraft.api.ComputerCraftAPI;
import net.neoforged.fml.common.Mod;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(GoonieSandersLocalExpanders.MOD_ID)
public class GoonieSandersLocalExpanders {
    public static final String MOD_ID = "gooniesanderslocalexpanders";
    private static final String HELPER_RESOURCE = "/assets/gooniesanderslocalexpanders/localscripts.lua";

    public GoonieSandersLocalExpanders() {
        installHelperIfMissing();
        ComputerCraftAPI.registerAPIFactory(LocalScriptApi::new);
        System.out.println("[GoonieSandersLocalExpanders] Initialized local script API!");
    }

    private void installHelperIfMissing() {
        Path helperPath = LocalScriptPaths.SCRIPT_DIR.resolve(LocalScriptPaths.DEFAULT_HELPER);
        if (Files.exists(helperPath)) return;

        try (InputStream stream = GoonieSandersLocalExpanders.class.getResourceAsStream(HELPER_RESOURCE)) {
            if (stream == null) {
                System.err.println("[GoonieSandersLocalExpanders] Could not locate bundled localscripts.lua");
                return;
            }

            Files.createDirectories(helperPath.getParent());
            Files.copy(stream, helperPath);
            System.out.println("[GoonieSandersLocalExpanders] Installed default localscripts.lua into config/ccscripts");
        } catch (IOException e) {
            System.err.println("[GoonieSandersLocalExpanders] Failed to install helper script: " + e.getMessage());
        }
    }
}

