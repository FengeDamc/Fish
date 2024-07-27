package com.fun.inject.injection.wrapper.impl.entity;


import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;
import com.fun.inject.injection.wrapper.impl.world.BlockPosWrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.fields.Fields;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.Wrapper;
import net.minecraft.world.entity.Entity;


import java.util.UUID;

public class EntityWrapper extends Wrapper {
    public Entity obj;

    public EntityWrapper(Entity entityObj) {
        super("net/minecraft/world/entity/Entity");
        this.obj = entityObj;
    }

    public EntityWrapper(String name) {
        super(name);
    }

    public EntityWrapper(int id) {
        super("net/minecraft/world/entity/Entity");
        this.obj = MinecraftWrapper.get().getWorld().getEntityByID(id).obj;
    }

    public EntityWrapper(Classes c) {
        super(c);
    }

    public UUID getUniqueID(){
        return (UUID) Methods.getUniqueID.invoke(obj);
    }
    public boolean isInvisible(){
        return (boolean)Methods.isInvisible.invoke(obj);
    }
    public EntityWrapper(Entity entityObj,String clazz) {
        super(clazz);

        this.obj = entityObj;
    }
    public boolean isSprinting() {
        return (boolean) Methods.isSprinting_Entity.invoke(obj);
    }
    public void setAngles(float yaw,float pitch) {
        Methods.setAngles_Entity.invoke(obj, yaw, pitch);
    }
    public float getEyeHeight(){
        //func_70047_e getEyeHeight
        return obj.getEyeHeight();

    }
    public boolean isDead(){
        return !obj.isAlive();//(boolean) ReflectionUtils.getFieldValue(obj,Mappings.getObfField("field_70128_L"));

    }



    public double getX() {
        // FD: pk/s net/minecraft/world/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70165_t");
        Object value = ReflectionUtils.getFieldValue(obj, notch);
        return value == null ? 0.0 : (Double) value;
    }
    public float getYaw() {
        // FD: pk/y net/minecraft/world/entity/Entity/field_70177_z
        return ((Entity)obj).getYRot();
    }

    public float getPitch() {
        // FD: pk/z net/minecraft/world/entity/Entity/field_70125_A
        return ((Entity)obj).getXRot();

    }
    public void setYaw(float yaw) {
        // FD: pk/y net/minecraft/world/entity/Entity/field_70177_z
        ((Entity)obj).setYRot(yaw);
    }

    public void setPitch(float pitch) {
        // FD: pk/z net/minecraft/world/entity/Entity/field_70125_A
        ((Entity)obj).setXRot(pitch);        //return value == null ? 0.0f : (Float) value;
    }

    public double getY() {
        // FD: pk/s net/minecraft/world/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70163_u");
        Object value = ReflectionUtils.getFieldValue(obj, notch);
        return value == null ? 0.0 : (Double) value;
    }
    public int getEntityID() {
        return (int) ReflectionUtils.invokeMethod(obj, Mappings.getObfMethod("func_145782_y"));
    }

    public double getZ() {
        // FD: pk/s net/minecraft/world/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70161_v");
        Object value = ReflectionUtils.getFieldValue(obj, notch);
        return value == null ? 0.0 : (Double) value;
    }

    public BlockPosWrapper getPos() {
        return new BlockPosWrapper(Math.floor(getX()), Math.floor(getY()), Math.floor(getZ()));
    }

    public Object getPosObj() {
        return BlockPosWrapper.create(getX(), getY(), getZ());
    }
}
