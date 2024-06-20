package net.fun.inject.inject.wrapper.impl.network;


import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.Wrapper;

public class NetworkManagerWrapper extends Wrapper {

    private final Object networkManagerObj;

    public NetworkManagerWrapper(Object obj) {
        super("net/minecraft/network/NetworkManager");
        networkManagerObj = obj;
    }

    public void processPacket(Object packet) {
        try {
            ReflectionUtils.invokeMethod(
                    networkManagerObj,
                    Mappings.getObfMethod("channelRead0"),
                    new Class[]{
                            Class.forName("io/netty/channel/ChannelHandlerContext"),
                            packet.getClass()
                    },
                    null,
                    packet
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
