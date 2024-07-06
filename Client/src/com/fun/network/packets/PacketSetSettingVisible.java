package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.client.settings.Setting;
import com.fun.network.IPacket;

public class PacketSetSettingVisible implements IPacket {
    public boolean visible;
    public int setting;
    public PacketSetSettingVisible(Setting s,boolean visible) {
        super();
        this.visible=visible;
        this.setting= FunGhostClient.settingsManager.settings.indexOf(s);
    }

    @Override
    public void process() {
        FunGhostClient.settingsManager.settings.get(setting).visible=(visible);
    }
}
