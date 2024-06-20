package net.fun.inject.inject.wrapper.impl.network.packets.server;

import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.impl.entity.EntityPlayerSPWrapper;
import net.fun.inject.inject.wrapper.impl.entity.EntityWrapper;
import net.fun.inject.inject.wrapper.impl.network.packets.PacketWrapper;
import net.fun.utils.Classes;

import static net.fun.client.utils.Rotation.Rotation.mc;

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
       return mc.getWorld().getEntityByID(getEntityID());
    }
    public int getEntityID(){
        return (int)ReflectionUtils.getFieldValue(packetObj,Mappings.getObfField("field_149417_a"));
    }
    public int getMotionX(){
        return (int)ReflectionUtils.getFieldValue(packetObj,Mappings.getObfField("field_149415_b"));
    }
    public int getMotionY(){
        return (int)ReflectionUtils.getFieldValue(packetObj,Mappings.getObfField("field_149416_c"));
    }
    public int getMotionZ(){
        return (int)ReflectionUtils.getFieldValue(packetObj,Mappings.getObfField("field_149414_d"));
    }
    public void setMotionX(int x){
        ReflectionUtils.setFieldValue(packetObj,Mappings.getObfField("field_149415_b"),x);
    }
    public void setMotionY(int x){
        ReflectionUtils.setFieldValue(packetObj,Mappings.getObfField("field_149416_c"),x);
    }
    public void setMotionZ(int x){
        ReflectionUtils.setFieldValue(packetObj,Mappings.getObfField("field_149414_d"),x);
    }


}
