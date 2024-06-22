package com.fun.inject.inject.wrapper.impl.entity;


import com.fun.inject.inject.wrapper.impl.world.BlockPosWrapper;
import com.fun.utils.Fields;
import com.fun.utils.Methods;
import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.ReflectionUtils;
import com.fun.inject.inject.wrapper.Wrapper;

import java.util.UUID;

public class EntityWrapper extends Wrapper {
    public Object entityObj;

    public EntityWrapper(Object entityObj) {
        super("net/minecraft/entity/Entity");
        this.entityObj = entityObj;
    }
    public UUID getUniqueID(){
        return (UUID) Methods.getUniqueID.invoke(entityObj);
    }
    public boolean isInvisible(){
        return (boolean)Methods.isInvisible.invoke(entityObj);
    }
    public EntityWrapper(Object entityObj,String clazz) {
        super(clazz);

        this.entityObj = entityObj;
    }
    public float getEyeHeight(){
        //func_70047_e getEyeHeight
        return (float) ReflectionUtils.invokeMethod(entityObj,Mappings.getObfMethod("func_70047_e"));

    }
    public boolean isDead(){
        return (boolean) ReflectionUtils.getFieldValue(entityObj,Mappings.getObfField("field_70128_L"));

    }
    public float getHealth(){
        return (float) ReflectionUtils.invokeMethod(entityObj,Mappings.getObfMethod("func_110143_aJ"));//func_110143_aJ
    }


    public double getX() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70165_t");
        Object value = ReflectionUtils.getFieldValue(entityObj, notch);
        return value == null ? 0.0 : (Double) value;
    }
    public float getYaw() {
        // FD: pk/y net/minecraft/entity/Entity/field_70177_z
        Object value = ReflectionUtils.getFieldValue(entityObj, Fields.Yaw_Entity.getName());
        return value == null ? 0.0f : (Float) value;
    }

    public float getPitch() {
        // FD: pk/z net/minecraft/entity/Entity/field_70125_A
        Object value = ReflectionUtils.getFieldValue(entityObj, Fields.Pitch_Entity.getName());
        return value == null ? 0.0f : (Float) value;
    }
    public void setYaw(float yaw) {
        // FD: pk/y net/minecraft/entity/Entity/field_70177_z
        ReflectionUtils.setFieldValue(entityObj, Fields.Yaw_Entity.getName(), yaw);
        //return value == null ? 0.0f : (Float) value;
    }

    public void setPitch(float pitch) {
        // FD: pk/z net/minecraft/entity/Entity/field_70125_A
        ReflectionUtils.setFieldValue(entityObj, Fields.Pitch_Entity.getName(),pitch);
        //return value == null ? 0.0f : (Float) value;
    }

    public double getY() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70163_u");
        Object value = ReflectionUtils.getFieldValue(entityObj, notch);
        return value == null ? 0.0 : (Double) value;
    }
    public int getEntityID() {
        return (int) ReflectionUtils.invokeMethod(entityObj, Mappings.getObfMethod("func_145782_y"));
    }

    public double getZ() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70161_v");
        Object value = ReflectionUtils.getFieldValue(entityObj, notch);
        return value == null ? 0.0 : (Double) value;
    }

    public BlockPosWrapper getPos() {
        return new BlockPosWrapper(Math.floor(getX()), Math.floor(getY()), Math.floor(getZ()));
    }

    public Object getPosObj() {
        return BlockPosWrapper.create(getX(), getY(), getZ());
    }
}
