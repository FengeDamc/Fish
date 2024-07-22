package com.fun.inject.injection.wrapper.impl.network.packets.server;

import com.fun.inject.injection.wrapper.impl.network.packets.PacketWrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;

public class S18PacketEntityTeleportWrapper extends PacketWrapper {
    public S18PacketEntityTeleportWrapper(Object in) {
        super(Classes.S18PacketEntityTeleport,in);

    }
    public int getEntityID() {
        //field_149074_a
        return(int) Methods.getEntityId_S18PacketEntityTeleport.invoke(obj);
    }
}
