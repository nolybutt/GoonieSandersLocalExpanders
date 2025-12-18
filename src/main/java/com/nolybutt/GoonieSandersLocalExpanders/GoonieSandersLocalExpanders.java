package com.nolybutt.GoonieSandersLocalExpanders;

import com.mojang.logging.LogUtils;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Mod(GoonieSandersLocalExpanders.MOD_ID)
public class GoonieSandersLocalExpanders {
    public static final String MOD_ID = "gooniesanderslocalexpanders";
    private static final String HELPER_RESOURCE = "/assets/gooniesanderslocalexpanders/localscripts.lua";
    static final Logger LOGGER = LogUtils.getLogger();

    public GoonieSandersLocalExpanders() {
        installHelperIfMissing();
        ComputerCraftAPI.registerAPIFactory(LocalScriptApi::new);
        LOGGER.info("[{}] Initialized local script API!", MOD_ID);
    }

    private void installHelperIfMissing() {
        Path helperPath = LocalScriptPaths.getScriptDir().resolve(LocalScriptPaths.DEFAULT_HELPER);
        if (Files.exists(helperPath)) return;

        try (InputStream stream = GoonieSandersLocalExpanders.class.getResourceAsStream(HELPER_RESOURCE)) {
            if (stream == null) {
                LOGGER.error("[{}] Could not locate bundled localscripts.lua", MOD_ID);
                return;
            }

            Files.createDirectories(helperPath.getParent());
            Files.copy(stream, helperPath);
            LOGGER.info("[{}] Installed default localscripts.lua into config/ccscripts", MOD_ID);
        } catch (IOException e) {
            LOGGER.error("[{}] Failed to install helper script", MOD_ID, e);
        }
    }
}

