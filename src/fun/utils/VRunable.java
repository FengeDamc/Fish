package fun.utils;

import fun.inject.Agent;
import fun.inject.inject.MinecraftType;
import fun.inject.inject.MinecraftVersion;

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
