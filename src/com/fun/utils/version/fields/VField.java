package com.fun.utils.version.fields;

import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.MinecraftType;
import com.fun.inject.inject.MinecraftVersion;
import com.fun.utils.version.Version;

import java.lang.reflect.Field;

public class VField extends Version {
    public Field field;
    public String friendly_name;
    public String obf_name;
    public Class<?> parent;
    VField(String fname, Class<?> parent){
        try {
            //parent=Agent.findClass(Mappings.getObfClass(parentName));
            obf_name= Mappings.getObfField(fname);
            this.parent=parent;
            field= parent.getField(obf_name);
            friendly_name=fname;
        } catch (NoSuchFieldException e) {

        }
    }
    VField(String fname){
        obf_name=Mappings.getObfField(fname);
        friendly_name=fname;
    }



    public VField version(MinecraftVersion version, MinecraftType type) {
        minecraftVersion=version;
        minecraftType=type;
        return this;
        //VersionNameManager.map.get(minecraftType).put(version,name);
    }
    public VField version(MinecraftVersion version) {
        version(version,MinecraftType.VANILLA);
        return this;
    }


    public VField version() {
        version(MinecraftVersion.VER_189);
        return this;
    }
}
