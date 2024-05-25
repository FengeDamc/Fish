package fun.inject.inject.wrapper.impl.other;


import fun.inject.inject.Mappings;
import fun.inject.inject.ReflectionUtils;
import fun.inject.inject.wrapper.Wrapper;
import fun.utils.Fields;

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
    public void setMoveForward(float forward) {
        ReflectionUtils.setFieldValue(movementInputObj, Mappings.getObfField("field_78900_b"),forward);
    }

    public void setMoveStrafe(float strafe) {
        ReflectionUtils.setFieldValue(movementInputObj, Mappings.getObfField("field_78902_a"),strafe);
    }
    public void setSneak(boolean sneak) {
        //FD: beu/d net/minecraft/util/MovementInput/field_78899_d

        ReflectionUtils.setFieldValue(movementInputObj, Fields.snake_MovementInput.getName(),sneak);
    }

    public boolean getSneak() {
        return (Boolean) ReflectionUtils.getFieldValue(movementInputObj, Fields.snake_MovementInput.getName());
    }
    public void setJump(boolean sneak) {
        //FD: beu/c net/minecraft/util/MovementInput/field_78901_c

        ReflectionUtils.setFieldValue(movementInputObj, Mappings.getObfField("field_78901_c"),sneak);
    }

    public boolean getJump() {
        return (Boolean) ReflectionUtils.getFieldValue(movementInputObj, Mappings.getObfField("field_78901_c"));
    }


}
