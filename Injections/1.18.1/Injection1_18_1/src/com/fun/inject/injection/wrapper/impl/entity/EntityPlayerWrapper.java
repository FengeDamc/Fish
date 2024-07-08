package com.fun.inject.injection.wrapper.impl.entity;


import com.fun.inject.injection.wrapper.impl.other.IChatComponentWrapper;
import com.fun.inject.injection.wrapper.impl.other.TeamWrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;


public class EntityPlayerWrapper extends EntityLivingBaseWrapper {
    

    public EntityPlayerWrapper(Entity entityObj) {
        super(Player.class.getName());
        this.obj =entityObj;
        //this.entityObj = entityObj;
    }

    public EntityPlayerWrapper(String name) {
        super(name);
    }

    public EntityPlayerWrapper(Classes c){
        super(c);
    }

    public TeamWrapper getTeam(){
        return new TeamWrapper(Methods.getTeam.invoke(obj));
    }

    public double getX() {
        // FD: pk/s net/minecraft/world/entity/Entity/field_70165_t

        return obj.getX();
    }


    public double getY() {
        // FD: pk/s net/minecraft/world/entity/Entity/field_70165_t

        return obj.getY();
    }

    public double getZ() {
        // FD: pk/s net/minecraft/world/entity/Entity/field_70165_t

        return obj.getZ();
    }

    public IChatComponentWrapper getDisplayName() {
        return new IChatComponentWrapper(((Player)obj).getDisplayName());
    }
}
