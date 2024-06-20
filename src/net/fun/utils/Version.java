package net.fun.utils;

import net.fun.inject.inject.MinecraftType;
import net.fun.inject.inject.MinecraftVersion;

public class Version {
    public MinecraftType minecraftType=MinecraftType.VANILLA;
    public MinecraftVersion minecraftVersion=MinecraftVersion.VER_189;


    public Version version(MinecraftVersion version, MinecraftType type) {
        minecraftVersion=version;
        minecraftType=type;
        return this;
        //VersionNameManager.map.get(minecraftType).put(version,name);
    }
    public Version version(MinecraftVersion version) {
        version(version,MinecraftType.VANILLA);
        return this;
    }


    public Version version() {
        version(MinecraftVersion.VER_189);
        return this;
    }
}
