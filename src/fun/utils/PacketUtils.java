package fun.utils;

import fun.inject.Agent;
import fun.inject.inject.Mappings;

public class PacketUtils {
    public static boolean isPacketInstanceof(Object packet, String packetName) {

        String unobfClass = Mappings.getUnobfClass(packet);
        //Agent.logger.info(unobfClass);
        return unobfClass.contains(packetName)||unobfClass.toLowerCase().contains(packetName.toLowerCase());
    }
}
