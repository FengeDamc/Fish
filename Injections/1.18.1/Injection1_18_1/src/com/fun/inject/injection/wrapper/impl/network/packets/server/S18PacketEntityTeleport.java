package com.fun.inject.injection.wrapper.impl.network.packets.server;

import com.fun.inject.injection.wrapper.impl.network.packets.PacketWrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import net.minecraft.network.protocol.Packet;

public class S18PacketEntityTeleport extends PacketWrapper {
    public S18PacketEntityTeleport(Object in) {
        super(Classes.S18PacketEntityTeleport,(Packet)in);

    }
    public int getEntityID() {
        //field_149074_a
        return(int) Methods.getEntityId_S18PacketEntityTeleport.invoke(obj);
    }
}
