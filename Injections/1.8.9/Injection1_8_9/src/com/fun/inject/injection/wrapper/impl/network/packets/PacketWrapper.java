package com.fun.inject.injection.wrapper.impl.network.packets;

import com.fun.inject.injection.wrapper.impl.network.INetHandler;
import com.fun.utils.version.clazz.Classes;
import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.methods.Methods;

public class PacketWrapper extends Wrapper {

    public PacketWrapper(String name) {
        super(name);
    }
    public PacketWrapper(String name,Object obj) {
        super(name);
        this.obj =obj;
    }
    public PacketWrapper(Classes name, Object obj) {
        super(name);
        this.obj =obj;
    }
    public void processPacket(INetHandler handler) {
        Methods.processPacket_Packet.invoke(obj, handler.obj);
    }
}
