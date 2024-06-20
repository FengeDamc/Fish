package net.fun.inject.inject.wrapper.impl.network;


import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.Wrapper;
import net.fun.utils.Methods;

import java.lang.reflect.Method;
import java.util.UUID;

public class NetHandlerPlayClientWrapper extends Wrapper {

    private final Object netHandlerPlayClientObj;

    public NetHandlerPlayClientWrapper(Object obj) {
        super("net/minecraft/client/network/NetHandlerPlayClient");
        netHandlerPlayClientObj = obj;
    }

    public void addToSendQueue(Object packet) {
        try {
            String notch = Mappings.getObfMethod("func_147297_a"); // addToSendQueue
            Class<?> packetClass = Class.forName(Mappings.getObfClass("net/minecraft/network/Packet"));
            Method method = getClass().getMethod(notch, packetClass);
            method.invoke(netHandlerPlayClientObj, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Object getPlayerInfo(UUID id){
        return Methods.getPlayerInfo_UUID_NetHandlerPlayClient.invoke(netHandlerPlayClientObj,id);
    }

    public NetworkManagerWrapper getNetworkManager() {
        return new NetworkManagerWrapper(ReflectionUtils.invokeMethod(netHandlerPlayClientObj, Mappings.getObfMethod("func_147298_b")));
    }
}
