package fun.inject.inject.asm.api;


import fun.inject.inject.InjectUtils;
import fun.inject.inject.Mappings;

public class Transformer {

    private final String name, obfName;
    private byte[] oldBytes;
    private Class<?> clazz;

    public Transformer(String name) {
        this.name = name;

        obfName = Mappings.getObfClass(name);
        if (obfName != null) {
            try {
                clazz = Class.forName(obfName);
                oldBytes = InjectUtils.getClassBytes(clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
