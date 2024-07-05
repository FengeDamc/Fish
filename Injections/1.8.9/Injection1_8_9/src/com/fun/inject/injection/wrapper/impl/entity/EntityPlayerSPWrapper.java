package com.fun.inject.injection.wrapper.impl.entity;


import com.fun.inject.injection.wrapper.impl.other.IChatComponentWrapper;
import com.fun.inject.injection.wrapper.impl.other.MovementInputWrapper;
import com.fun.inject.injection.wrapper.impl.world.BlockPosWrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.fields.Fields;
import com.fun.utils.version.methods.Methods;
import com.fun.utils.math.vecmath.Vec3;
import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import net.minecraft.client.Minecraft;

import javax.vecmath.Vector3f;
import java.lang.reflect.Field;

public class EntityPlayerSPWrapper extends EntityPlayerWrapper {
    public float rotationPitch;
    private Object sendQueueObj;

    private MovementInputWrapper movementInputObj;

    public EntityPlayerSPWrapper() {
        super(Classes.EntityPlayerSP);
    }

    public boolean isNull() {
        return obj == null;
    }
    public IChatComponentWrapper getDisplayName() {
        return new IChatComponentWrapper(Methods.getDisplayName_EntityPlayer.invoke(obj));
    }


    public double getX() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Fields.posX_Entity.getName();
        Object value = ReflectionUtils.getFieldValue(obj, notch);
        return value == null ? 0.0 : (Double) value;
    }
    //movementInput FD: bew/b net/minecraft/client/entity/EntityPlayerSP/field_71158_b


    public double getY() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Fields.posY_Entity.getName();
        Object value = ReflectionUtils.getFieldValue(obj, notch);
        return value == null ? 0.0 : (Double) value;
    }

    public double getZ() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Fields.posZ_Entity.getName();
        Object value = ReflectionUtils.getFieldValue(obj, notch);
        return value == null ? 0.0 : (Double) value;
    }



    public double getMotionX() {
        return (Double) ReflectionUtils.getFieldValue(obj, Fields.MotionX.getName());
    }

    public double getMotionY() {
        return (Double) ReflectionUtils.getFieldValue(obj, Fields.MotionY.getName());
    }

    public double getMotionZ() {
        return (Double) ReflectionUtils.getFieldValue(obj, Fields.MotionZ.getName());
    }

    public void setMotionX(double motionX) {
        ReflectionUtils.setFieldValue(obj, Fields.MotionX.getName(), motionX);
    }

    public void setMotionY(double motionY) {
        ReflectionUtils.setFieldValue(obj, Fields.MotionY.getName(), motionY);
    }

    public void setMotionZ(double motionZ) {
        ReflectionUtils.setFieldValue(obj,Fields.MotionZ.getName(), motionZ);
    }

    public boolean isOnGround() {
        // FD: pk/C net/minecraft/entity/Entity/field_70122_E
        return (Boolean) ReflectionUtils.getFieldValue(obj,Fields.OnGround_Entity.getName());
    }

    public float getFallDistance() {
        return (Float) ReflectionUtils.getFieldValue(obj, Mappings.getObfField("field_70143_R"));
    }

    public void setFallDistance(float fallDistance) {
        ReflectionUtils.setFieldValue(obj, Mappings.getObfField("field_70143_R"), fallDistance);
    }

    public float getSpeedInAir() {
        return (Float) ReflectionUtils.getFieldValue(obj, Mappings.getObfField("field_71102_ce"));
    }
    public boolean isOpenGui(){
        return Minecraft.getMinecraft().currentScreen != null;
    }

    public void setSpeedInAir(float speedInAir) {
        ReflectionUtils.setFieldValue(obj, Mappings.getObfField("field_71102_ce"), speedInAir);
    }

    public boolean isInWater() {
        return (Boolean) ReflectionUtils.invokeMethod(obj, Mappings.getObfMethod("func_70090_H"));
    }

    public boolean isInLava() {
        return (Boolean) ReflectionUtils.invokeMethod(obj, Mappings.getObfMethod("func_180799_ab"));
    }

    public boolean isMoving() {
        return this.getMovementInputObj().getMoveForward() != 0 || this.getMovementInputObj().getMoveStrafe() != 0;
    }

    public void jump() {
        ReflectionUtils.invokeMethod(obj, Mappings.getObfMethod("func_70664_aZ"));
    }

    public BlockPosWrapper getPos() {
        return new BlockPosWrapper(Math.floor(getX()), Math.floor(getY()), Math.floor(getZ()));
    }
    public Vec3 getPosVec() {
        return new Vec3(getX(),getY(),getZ());//new BlockPosWrapper(Math.floor(getX()), Math.floor(getY()), Math.floor(getZ()));
    }

    public Object getPosObj() {
        return BlockPosWrapper.create(getX(), getY(), getZ());
    }

    public double getDistance(EntityWrapper wrapper) {
        float f = (float) (getX() - wrapper.getX());
        float f1 = (float) (getY() - wrapper.getY());
        float f2 = (float) (getZ() - wrapper.getZ());
        return Math.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public float getEyeHeight() {
        float value = 1.62f;
        if (isSneaking()) {
            value -= 0.08f;
        }
        return value;
    }

    public float getYaw() {
        // FD: pk/y net/minecraft/entity/Entity/field_70177_z
        Object value = ReflectionUtils.getFieldValue(obj, Fields.Yaw_Entity.getName());
        return value == null ? 0.0f : (Float) value;
    }

    public float getPitch() {
        // FD: pk/z net/minecraft/entity/Entity/field_70125_A
        Object value = ReflectionUtils.getFieldValue(obj, Fields.Pitch_Entity.getName());
        return value == null ? 0.0f : (Float) value;
    }
    public void setYaw(float yaw) {
        // FD: pk/y net/minecraft/entity/Entity/field_70177_z
        ReflectionUtils.setFieldValue(obj, Fields.Yaw_Entity.getName(), yaw);
        //return value == null ? 0.0f : (Float) value;
    }

    public void setPitch(float pitch) {
        // FD: pk/z net/minecraft/entity/Entity/field_70125_A
        ReflectionUtils.setFieldValue(obj, Fields.Pitch_Entity.getName(),pitch);
        //return value == null ? 0.0f : (Float) value;
    }


    public Vector3f getVectorForRotation() {
        float pitch = getPitch();
        float yaw = getYaw();

        float f = (float) Math.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = (float) Math.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = (float) -Math.cos(-pitch * 0.017453292F);
        float f3 = (float) Math.sin(-pitch * 0.017453292F);
        return new Vector3f(f1 * f2, f3, f * f2);
    }

    public void setSprinting(boolean value) {
        if (obj == null) return;

        // MD: bew/d (Z)V net/minecraft/client/entity/EntityPlayerSP/func_70031_b (Z)V

        /*String notch = Methods.setSprinting.getName(); // setSprinting
        try {
            Method m = getClazz().getMethod(notch, boolean.class);
            m.invoke(entityObj, value);
        } catch (Exception e) {
            e.printStackTrace();

        }*/
        ReflectionUtils.invokeMethod(obj,Classes.EntityPlayerSP,Methods.setSprinting,true);
    }



    public boolean isSneaking() {
        return (boolean)Methods.isSneaking_EntityPlayerSP.invoke(obj);
    }

    public Object getSendQueue() {
        if (sendQueueObj == null) {
            try {
                String notch = Fields.sendQueueSP.getName(); // sendQueue
                Field field = getClazz().getField(notch);
                sendQueueObj = field.get(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sendQueueObj;
    }

    public MovementInputWrapper getMovementInputObj() {
        return new MovementInputWrapper(ReflectionUtils.getFieldValue(obj, Mappings.getObfField("field_71158_b")));
    }

    public void addChatMessage(Object chatComponentText) {
        ReflectionUtils.invokeMethod(obj, Mappings.getObfMethod("func_145747_a"), new Class[]{chatComponentText.getClass()}, chatComponentText);
    }

    public void sendChatMessage(String message) {
        ReflectionUtils.invokeMethod(obj, Mappings.getObfMethod("func_71165_d"), new Class[]{String.class}, message);
    }

    public void setEntityObj(Object entityObj) {
        this.obj = entityObj;
    }

    public Object getEntityObj() {
        return obj;
    }

    public boolean isInFluid() {
        return isInWater() || isInLava();
    }

    public int getEntityID() {
        return (int) ReflectionUtils.invokeMethod(obj, Methods.getEntityID_Entity.getName());
    }

    public boolean isUsingItem() {
        return (boolean)ReflectionUtils.invokeMethod(obj, Methods.isUsing.getName());

    }
}
