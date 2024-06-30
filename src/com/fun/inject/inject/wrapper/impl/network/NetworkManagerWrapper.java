package com.fun.inject.inject.wrapper.impl.network;


import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.ReflectionUtils;
import com.fun.inject.inject.wrapper.Wrapper;
import com.fun.inject.inject.wrapper.impl.network.packets.PacketWrapper;
import com.fun.utils.Fields;
import com.fun.utils.Methods;
import io.netty.channel.Channel;

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
    public void processPacketNoEvent(PacketWrapper packet) {
        if(getChannel().isOpen()){
            try{
                packet.processPacket(getPacketListener());
            }
            catch(Exception e){

            }
        }
    }
    public Channel getChannel() {
        return (Channel) Fields.channel_NetworkManager.get(networkManagerObj);
    }
    public INetHandler getPacketListener() {
        return new INetHandler(Fields.packetListener_NetworkManager.get(networkManagerObj));
    }
    public void sendPacket(PacketWrapper packet) {
        Methods.sendPacket_NetworkManager.invoke(networkManagerObj, packet.obj);
    }

}
