package net.fun.utils;

import net.fun.inject.Agent;
import net.fun.inject.inject.MinecraftType;
import net.fun.inject.inject.MinecraftVersion;

public abstract class VRunable extends Version implements Runnable{
    @Override
    public VRunable version(MinecraftVersion version, MinecraftType type) {
        return (VRunable) super.version(version, type);
    }

    @Override
    public VRunable version(MinecraftVersion version) {
        return (VRunable) super.version(version);
    }

    @Override
    public VRunable version() {
        return (VRunable) super.version();
    }
    public static void runV(VRunable... vrs){
        for(VRunable vr:vrs){
            if(vr.minecraftType== Agent.minecraftType&&
            vr.minecraftVersion==Agent.minecraftVersion){
                vr.run();
                return;
            }
        }
        for(VRunable vr:vrs){
            if(vr.minecraftVersion==Agent.minecraftVersion){
                vr.run();
                return;
            }
        }
        vrs[0].run();
    }
}
