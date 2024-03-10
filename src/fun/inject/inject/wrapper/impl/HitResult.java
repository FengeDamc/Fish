package fun.inject.inject.wrapper.impl;



import fun.inject.inject.Mappings;
import fun.inject.inject.ReflectionUtils;

public class HitResult {

    private Type type;

    public HitResult(Object objectMouseOverObj) {
        try {
            Object value = ReflectionUtils.getFieldValue(objectMouseOverObj, Mappings.getObfField("field_72313_a"));
            type = Type.valueOf(((Enum<?>) value).name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Type getType() {
        return type == null ? Type.MISS : type;
    }

    public enum Type {
        MISS, BLOCK, ENTITY
    }
}
