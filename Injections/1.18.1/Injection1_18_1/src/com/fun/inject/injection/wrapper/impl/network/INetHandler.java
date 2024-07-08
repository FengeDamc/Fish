package com.fun.inject.injection.wrapper.impl.network;

import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.clazz.Classes;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.PacketListener;

public class INetHandler extends Wrapper {
    public PacketListener obj;
    public INetHandler(PacketListener obj) {
        super(Classes.INetHandler);
        this.obj = obj;
    }
}
