package net.fun.utils;

import net.fun.inject.Agent;
import net.fun.inject.inject.Mappings;

public class PacketUtils {
    public static boolean isPacketInstanceof(Object packet, String packetName) {

        String unobfClass = Mappings.getUnobfClass(packet);
        //Agent.logger.info(unobfClass);
        return unobfClass.contains(packetName)||unobfClass.toLowerCase().contains(packetName.toLowerCase());
    }
}
