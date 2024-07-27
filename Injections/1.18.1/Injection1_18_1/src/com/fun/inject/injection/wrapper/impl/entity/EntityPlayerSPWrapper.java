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
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import javax.vecmath.Vector3f;
import java.lang.reflect.Field;

public class EntityPlayerSPWrapper extends EntityPlayerWrapper {
    public float rotationPitch;
    private Object sendQueueObj;

    private MovementInputWrapper movementInputObj;

    public EntityPlayerSPWrapper() {
        super(LocalPlayer.class.getName());
    }

    public boolean isNull() {
        return obj == null;
    }
    public IChatComponentWrapper getDisplayName() {
        return new IChatComponentWrapper(obj.getDisplayName());
    }



    public boolean isOpenGui(){
        return Minecraft.getInstance().screen!=null;
    }
    //movementInput FD: bew/b net/minecraft/client/entity/EntityPlayerSP/field_71158_b








    public double getMotionX() {
        return obj.getDeltaMovement().x;//(Double) ReflectionUtils.getFieldValue(obj, Fields.MotionX.getName());
    }

    public double getMotionY() {
        return obj.getDeltaMovement().y;
    }

    public double getMotionZ() {
        return obj.getDeltaMovement().z;
    }

    public void setMotionX(double motionX) {
        obj.setDeltaMovement(motionX,
                obj.getDeltaMovement().y,obj.getDeltaMovement().z);//ReflectionUtils.setFieldValue(obj, Fields.MotionX.getName(), motionX);
    }

    public void setMotionY(double motionY) {
        obj.setDeltaMovement(obj.getDeltaMovement().x,
                motionY,obj.getDeltaMovement().z);
    }

    public void setMotionZ(double motionZ) {
        obj.setDeltaMovement(obj.getDeltaMovement().x,obj.getDeltaMovement().y
                ,motionZ);
    }

    public boolean isOnGround() {
        // FD: pk/C net/minecraft/world/entity/Entity/field_70122_E
        return obj.isOnGround();
    }

    public float getFallDistance() {
        return obj.fallDistance;
    }

    public void setFallDistance(float fallDistance) {
        obj.fallDistance=fallDistance;//ReflectionUtils.setFieldValue(obj, Mappings.getObfField("field_70143_R"), fallDistance);
    }

    public float getSpeedInAir() {
        return (Float) ReflectionUtils.getFieldValue(obj, Mappings.getObfField("field_71102_ce"));
    }

    public void setSpeedInAir(float speedInAir) {
        ReflectionUtils.setFieldValue(obj, Mappings.getObfField("field_71102_ce"), speedInAir);
    }

    public boolean isInWater() {
        return ((Player)obj).isInWater();//(Boolean) ReflectionUtils.invokeMethod(obj, Mappings.getObfMethod("func_70090_H"));
    }

    public boolean isInLava() {
        return ((Player)obj).isInLava();
    }

    public boolean isMoving() {
        return this.getMovementInputObj().getMoveForward() != 0 || this.getMovementInputObj().getMoveStrafe() != 0;
    }

    public void jump() {
        ((Player)obj).jumpFromGround();//ReflectionUtils.invokeMethod(obj, Mappings.getObfMethod("func_70664_aZ"));
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
        obj.setSprinting(value);
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

    public EntityPlayerSPWrapper setEntityObj(Entity entityObj) {
        this.obj = entityObj;
        return this;
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
