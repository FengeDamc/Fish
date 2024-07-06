package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.network.IPacket;

public class PacketSetString implements IPacket {
    public PacketSetString(int id, String str) {
        this.settingIndex=id;
        this.value=str;
    }

    public int settingIndex;
    public String value;
    @Override
    public void process() {
        FunGhostClient.settingsManager.getSettings().get(settingIndex).setValString(value);

    }
}
