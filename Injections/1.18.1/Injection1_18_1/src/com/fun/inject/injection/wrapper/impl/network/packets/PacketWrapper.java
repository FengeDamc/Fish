package com.fun.inject.injection.wrapper.impl.network.packets;

import com.fun.inject.injection.wrapper.impl.network.INetHandler;
import com.fun.utils.version.clazz.Classes;
import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.methods.Methods;
import net.minecraft.network.protocol.Packet;

public class PacketWrapper extends Wrapper {
    public Packet obj;

    public PacketWrapper(String name) {
        super(name);
    }
    public PacketWrapper(String name,Packet<?> obj) {
        super(name);
        this.obj =obj;
    }
    public PacketWrapper(Classes name, Packet<?> obj) {
        super(name);
        this.obj =obj;
    }
    public void processPacket(INetHandler handler) {
        obj.handle(handler.obj);//Methods.processPacket_Packet.invoke(obj, handler);
    }
}
