package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.network.IPacket;

public class PacketSetBool implements IPacket {
    public PacketSetBool(int settingIndex,boolean value) {
        this.settingIndex = settingIndex;
        this.value = value;
    }

    public int settingIndex;
    public boolean value;
    @Override
    public void process() {
        FunGhostClient.settingsManager.getSettings().get(settingIndex).setValBoolean(value);

    }
}
