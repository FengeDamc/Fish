package fun.inject.inject.asm.api;


import fun.inject.Agent;
import fun.inject.inject.InjectUtils;
import fun.inject.inject.Mappings;
import fun.utils.Classes;

public class Transformer {

    public final String name, obfName;
    public byte[] oldBytes;
    public byte[] newBytes;

    public Class<?> clazz;

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
