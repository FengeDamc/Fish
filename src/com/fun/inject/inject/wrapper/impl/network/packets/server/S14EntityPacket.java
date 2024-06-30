package com.fun.inject.inject.wrapper.impl.network.packets.server;

import com.fun.inject.inject.wrapper.impl.network.packets.PacketWrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.fields.Fields;

public class S14EntityPacket extends PacketWrapper {
    public S14EntityPacket(Object obj) {
        super(Classes.S14PacketEntity, obj);
    }

    public int getEntityID() {
        //field_149074_a
        return(int) Fields.entityId_S14PacketEntity.get(obj);
    }
}
