package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.network.IPacket;

public class PacketSetRun implements IPacket {
    public PacketSetRun(int settingIndex, boolean value) {
        this.settingIndex = settingIndex;
        this.value = value;
    }

    public int settingIndex;
    public boolean value;
    @Override
    public void process() {
        FunGhostClient.registerManager.mods.get(settingIndex).setRunning(value);
    }
}
