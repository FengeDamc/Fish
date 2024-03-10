package fun.inject.inject.wrapper.impl.other;


import fun.inject.inject.Mappings;
import fun.inject.inject.ReflectionUtils;
import fun.inject.inject.wrapper.Wrapper;

public class MovementInputWrapper extends Wrapper {

    private final Object movementInputObj;

    public MovementInputWrapper(Object obj) {
        super("net/minecraft/util/MovementInput");
        movementInputObj = obj;
    }

    public float getMoveForward() {
        return (Float) ReflectionUtils.getFieldValue(movementInputObj, Mappings.getObfField("field_78900_b"));
    }

    public float getMoveStrafe() {
        return (Float) ReflectionUtils.getFieldValue(movementInputObj, Mappings.getObfField("field_78902_a"));
    }
}
