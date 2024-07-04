package com.fun.utils.packet;

import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;
import com.fun.inject.injection.wrapper.impl.network.packets.PacketWrapper;

public class PacketUtils {
    public static MinecraftWrapper mc=MinecraftWrapper.get();
    public static void receivePacketNoEvent(PacketWrapper packet)
    {
        mc.getNetHandler().getNetworkManager().processPacketNoEvent(packet);

    }
    public static void sendPacketNoEvent(PacketWrapper packet){
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }
}
