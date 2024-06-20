package net.fun.inject.inject.wrapper.impl.other;


import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.Wrapper;
import net.fun.utils.Fields;

public class MovementInputWrapper extends Wrapper {

    private final Object movementInputObj;

    public MovementInputWrapper(Object obj) {
        super("net/minecraft/util/MovementInput");
        movementInputObj = obj;
    }

    public float getMoveForward() {
        return (float)Fields.moveForward_MovementInput.get(movementInputObj);
    }

    public float getMoveStrafe() {
        return (float) Fields.moveStrafe_MovementInput.get(movementInputObj);
    }
    public void setMoveForward(float forward) {
        Fields.moveForward_MovementInput.set(movementInputObj,forward);
    }

    public void setMoveStrafe(float strafe) {
        Fields.moveStrafe_MovementInput.set(movementInputObj,strafe);
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
