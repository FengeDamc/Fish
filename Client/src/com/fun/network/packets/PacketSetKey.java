package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.network.IPacket;

public class PacketSetKey implements IPacket {
    public PacketSetKey(int settingIndex, int value) {
        this.settingIndex = settingIndex;
        this.value = value;
    }

    public int settingIndex;
    public int value;
    @Override
    public void process() {
        FunGhostClient.moduleManager.mods.get(settingIndex).setKey(value);
    }
}
