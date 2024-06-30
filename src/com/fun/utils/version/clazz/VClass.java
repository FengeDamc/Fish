package com.fun.utils.version.clazz;

import com.fun.inject.Agent;
import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.MinecraftType;
import com.fun.inject.inject.MinecraftVersion;
import com.fun.utils.version.Version;

public class VClass extends Version {
    public String obf_name ="";
    public String friendly_name ="";
    public Class<?> clazz =null;

    VClass(String name){
            name=name.replace('.','/');
            this.obf_name = Mappings.getObfClass(name);
            this.friendly_name=name;
            try {
                this.clazz = Agent.findClass(this.obf_name);
            } catch (ClassNotFoundException e) {
                if(this.clazz == null){
                    try {
                        this.clazz = Class.forName(this.obf_name);
                    } catch (ClassNotFoundException e1) {
                    }
                }
            }


    }

    public boolean isInstanceof(Object obj){
        return obj.getClass() == clazz;
    }



    public VClass version(MinecraftType type,MinecraftVersion version) {
        minecraftVersion=version;
        minecraftType=type;
        return this;
        //VersionNameManager.map.get(minecraftType).put(version,name);
    }
    public VClass version(MinecraftVersion version) {
        version(version,MinecraftType.VANILLA);
        return this;
    }


    public VClass version() {
        version(MinecraftVersion.VER_189);
        return this;
    }
}
