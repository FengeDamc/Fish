package net.fun.inject.inject.wrapper.impl.entity;


import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.impl.other.IChatComponentWrapper;
import net.fun.inject.inject.wrapper.impl.other.TeamWrapper;
import net.fun.utils.Classes;
import net.fun.utils.Methods;


public class EntityPlayerWrapper extends EntityWrapper {
    //private final Object entityObj;

    public EntityPlayerWrapper(Object entityObj) {
        super(entityObj,"net/minecraft/entity/player/EntityPlayer");
        //this.entityObj = entityObj;
    }
    public EntityPlayerWrapper(Classes c){
        super(c);
    }

    public TeamWrapper getTeam(){
        return new TeamWrapper(Methods.getTeam.invoke(entityObj));
    }

    public double getX() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70165_t");
        Object value = ReflectionUtils.getFieldValue(entityObj, notch);
        return value == null ? 0.0 : (Double) value;
    }


    public double getY() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70163_u");
        Object value = ReflectionUtils.getFieldValue(entityObj, notch);
        return value == null ? 0.0 : (Double) value;
    }

    public double getZ() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70161_v");
        Object value = ReflectionUtils.getFieldValue(entityObj, notch);
        return value == null ? 0.0 : (Double) value;
    }

    public IChatComponentWrapper getDisplayName() {
        return new IChatComponentWrapper(Methods.getDisplayName_EntityPlayer.invoke(entityObj));
    }
}
