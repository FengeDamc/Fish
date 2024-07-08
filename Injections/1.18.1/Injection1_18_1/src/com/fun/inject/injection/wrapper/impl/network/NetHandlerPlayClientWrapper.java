package com.fun.inject.injection.wrapper.impl.network;


import com.fun.utils.version.methods.Methods;
import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.Wrapper;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;

import java.lang.reflect.Method;
import java.util.UUID;

public class NetHandlerPlayClientWrapper extends Wrapper {

    private final ClientPacketListener netHandlerPlayClientObj;

    public NetHandlerPlayClientWrapper(ClientPacketListener obj) {
        super("net/minecraft/client/network/NetHandlerPlayClient");
        netHandlerPlayClientObj = obj;
    }

    public void addToSendQueue(Packet<?> packet) {
       netHandlerPlayClientObj.send(packet);
    }
    public Object getPlayerInfo(UUID id){
        return netHandlerPlayClientObj.getPlayerInfo(id);
    }

    public NetworkManagerWrapper getNetworkManager() {
        return new NetworkManagerWrapper(netHandlerPlayClientObj.getConnection());//new NetworkManagerWrapper(ReflectionUtils.invokeMethod(netHandlerPlayClientObj, Mappings.getObfMethod("func_147298_b")));
    }
}
