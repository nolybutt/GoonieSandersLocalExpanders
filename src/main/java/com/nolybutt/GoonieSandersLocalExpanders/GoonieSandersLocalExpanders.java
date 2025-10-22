package com.nolybutt.GoonieSandersLocalExpanders;

import dan200.computercraft.api.ComputerCraftAPI;
import net.neoforged.fml.common.Mod;

@Mod(GoonieSandersLocalExpanders.MOD_ID)
public class GoonieSandersLocalExpanders {
    public static final String MOD_ID = "gooniesanderslocalexpanders";

    public GoonieSandersLocalExpanders() {
        ComputerCraftAPI.registerAPIFactory(computer -> new LocalScriptApi());
        System.out.println("[GoonieSandersLocalExpanders] Initialized local script API!");
    }
}

