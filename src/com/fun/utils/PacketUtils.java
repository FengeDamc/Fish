package com.fun.utils;

import com.fun.inject.inject.Mappings;

public class PacketUtils {
    public static boolean isPacketInstanceof(Object packet, String packetName) {

        String unobfClass = Mappings.getUnobfClass(packet);
        //Agent.System.out.println(unobfClass);
        return unobfClass.contains(packetName)||unobfClass.toLowerCase().contains(packetName.toLowerCase());
    }
}
