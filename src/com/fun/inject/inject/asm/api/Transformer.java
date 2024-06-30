package com.fun.inject.inject.asm.api;


import com.fun.utils.version.clazz.Classes;
import com.fun.inject.Agent;
import com.fun.inject.inject.Mappings;

public class Transformer {

    public String name, obfName;
    public byte[] oldBytes;
    public byte[] newBytes;

    public Class<?> clazz;

    public Transformer() {
        super();
    }

    public Transformer(String name) {
        this.name = name;

        obfName = Mappings.getObfClass(name);
        if (obfName != null) {
            try {
                clazz = Agent.findClass(obfName);
                //oldBytes = InjectUtils.getClassBytes(clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Transformer(Classes classes){
        this.name=classes.getVClass().friendly_name;
        this.obfName= classes.getVClass().obf_name;
        this.clazz=classes.getClazz();
    }

    public byte[] getOldBytes() {
        return oldBytes;
    }

    public String getName() {
        return name;
    }

    public String getObfName() {
        return obfName;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
