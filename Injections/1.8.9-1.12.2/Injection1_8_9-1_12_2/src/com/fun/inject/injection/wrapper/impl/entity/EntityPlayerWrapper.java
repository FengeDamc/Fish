package com.fun.inject.injection.wrapper.impl.entity;


import com.fun.inject.injection.wrapper.impl.other.IChatComponentWrapper;
import com.fun.inject.injection.wrapper.impl.other.TeamWrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;


public class EntityPlayerWrapper extends EntityLivingBaseWrapper {
    //private final Object entityObj;

    public EntityPlayerWrapper(Object entityObj) {
        super(Classes.EntityPlayer);
        this.obj =entityObj;
        //this.entityObj = entityObj;
    }
    public EntityPlayerWrapper(Classes c){
        super(c);
    }

    public TeamWrapper getTeam(){
        return new TeamWrapper(Methods.getTeam.invoke(obj));
    }

    public double getX() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70165_t");
        Object value = ReflectionUtils.getFieldValue(obj, notch);
        return value == null ? 0.0 : (Double) value;
    }


    public double getY() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70163_u");
        Object value = ReflectionUtils.getFieldValue(obj, notch);
        return value == null ? 0.0 : (Double) value;
    }

    public double getZ() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70161_v");
        Object value = ReflectionUtils.getFieldValue(obj, notch);
        return value == null ? 0.0 : (Double) value;
    }

    public IChatComponentWrapper getDisplayName() {
        return new IChatComponentWrapper(Methods.getDisplayName_EntityPlayer.invoke(obj));
    }
}
