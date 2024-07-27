package com.fun.utils;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;

public class PacketUtils {
    public static Minecraft mc=Minecraft.getInstance();
    public static void receivePacketNoEvent(Packet packet) {
        mc.execute(()->{
            packet.handle(mc.getConnection().getConnection().getPacketListener());
        });
    }
    public static void receivePacket(Packet packet) {
        if(!EventManager.call(new EventPacket(packet)).cancel)packet.handle(mc.getConnection().getConnection().getPacketListener());

    }
    public static void sendPacketNoEvent(Packet packet) {
        mc.getConnection().getConnection().send(packet);
    }
    public static void sendPacket(Packet packet) {
        mc.getConnection().send(packet);
    }

}
