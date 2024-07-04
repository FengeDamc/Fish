package com.fun.utils.version;

import com.fun.inject.MinecraftType;
import com.fun.inject.MinecraftVersion;

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
