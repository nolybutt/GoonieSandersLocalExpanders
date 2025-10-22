package com.nolybutt.GoonieSandersLocalExpanders;

import dan200.computercraft.api.peripheral.PeripheralAPI;
import net.neoforged.fml.common.Mod;

@Mod("GoonieSandersLocalExpanders")
public class GoonieSandersLocalExpanders {
    public GoonieSandersLocalExpanders() {
        PeripheralAPI.registerGlobalPeripheral("GoonieSandersLocalExpanders", new LocalScriptPeripheral());
        System.out.println("[GoonieSandersLocalExpanders] Initialized and registered global peripheral!");
    }
}

