package fun.utils;

import fun.inject.Agent;
import fun.inject.inject.Mappings;
import fun.inject.inject.MinecraftType;
import fun.inject.inject.MinecraftVersion;

public class VClass extends Version{
    public String obf_name ="";
    public String friendly_name ="";
    public Class<?> clazz =null;

    VClass(String name){
            this.obf_name = Mappings.getObfClass(name);
            this.friendly_name=name;
            this.clazz = Agent.findClass(this.obf_name);

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
