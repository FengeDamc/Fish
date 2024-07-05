package com.fun.inject.injection.wrapper.impl.network.packets.server;

import com.fun.client.utils.Rotation.Rotation;
import com.fun.inject.injection.wrapper.impl.entity.EntityWrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.impl.network.packets.PacketWrapper;

public class S12PacketEntityVelocity extends PacketWrapper {

    public S12PacketEntityVelocity(Object obj) {
        super(Classes.S12PACKET_ENTITY_VELOCITY, obj);
    }
    //FD: hm/a net/minecraft/network/play/server/S12PacketEntityVelocity/field_149417_a entityId
    //FD: hm/b net/minecraft/network/play/server/S12PacketEntityVelocity/field_149415_b motionX
    //FD: hm/c net/minecraft/network/play/server/S12PacketEntityVelocity/field_149416_c motionY
    //FD: hm/d net/minecraft/network/play/server/S12PacketEntityVelocity/field_149414_d motionZ


    //func_73045_a,getEntityByID,2,"Returns the Entity with the given ID, or null if it doesn't exist in this World."
    //MD: adm/a (I)Lpk; net/minecraft/world/World/func_73045_a (I)Lnet/minecraft/entity/Entity;
    public EntityWrapper getEntity(){
       return Rotation.mc.getWorld().getEntityByID(getEntityID());
    }
    public int getEntityID(){
        return (int)ReflectionUtils.getFieldValue(obj,Mappings.getObfField("field_149417_a"));
    }
    public int getMotionX(){
        return (int)ReflectionUtils.getFieldValue(obj,Mappings.getObfField("field_149415_b"));
    }
    public int getMotionY(){
        return (int)ReflectionUtils.getFieldValue(obj,Mappings.getObfField("field_149416_c"));
    }
    public int getMotionZ(){
        return (int)ReflectionUtils.getFieldValue(obj,Mappings.getObfField("field_149414_d"));
    }
    public void setMotionX(int x){
        ReflectionUtils.setFieldValue(obj,Mappings.getObfField("field_149415_b"),x);
    }
    public void setMotionY(int x){
        ReflectionUtils.setFieldValue(obj,Mappings.getObfField("field_149416_c"),x);
    }
    public void setMotionZ(int x){
        ReflectionUtils.setFieldValue(obj,Mappings.getObfField("field_149414_d"),x);
    }


}
