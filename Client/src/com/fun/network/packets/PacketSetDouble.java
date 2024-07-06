package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.network.IPacket;

public class PacketSetDouble implements IPacket {
    public PacketSetDouble(int id, double str) {
        this.settingIndex=id;
        this.value=str;
    }

    public int settingIndex;
    public double value;
    @Override
    public void process() {
        FunGhostClient.settingsManager.getSettings().get(settingIndex).setValDouble(value);

    }
}
