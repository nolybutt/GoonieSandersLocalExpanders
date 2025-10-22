package com.nolybutt.GoonieSandersLocalExpanders;

import dan200.computercraft.api.ComputerCraftAPI;
import net.neoforged.fml.common.Mod;

@Mod("GoonieSandersLocalExpanders")
public class GoonieSandersLocalExpanders {
    public GoonieSandersLocalExpanders() {
        ComputerCraftAPI.registerAPIFactory(computer -> new LocalScriptApi());
        System.out.println("[GoonieSandersLocalExpanders] Initialized local script API!");
    }
}

