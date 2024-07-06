package com.fun.network.packets;

import com.fun.network.IPacket;

import java.lang.reflect.InvocationTargetException;

public class PacketDestroy implements IPacket {
    @Override
    public void process() {
        try {
            ClassLoader.getSystemClassLoader().loadClass("com.fun.inject.InjectUtils").getDeclaredMethod("destroyClient").invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
