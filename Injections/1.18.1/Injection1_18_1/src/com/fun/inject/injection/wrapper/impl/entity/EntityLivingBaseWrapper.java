package com.fun.inject.injection.wrapper.impl.entity;

import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.utils.version.clazz.Classes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class EntityLivingBaseWrapper extends EntityWrapper {
    public EntityLivingBaseWrapper(LivingEntity entity) {
        super(LivingEntity.class.getName());
        this.obj = entity;
    }
    public float getHealth(){
        return ((LivingEntity)obj).getHealth();//func_110143_aJ
    }

    public EntityLivingBaseWrapper(String name) {
        super(name);
    }

    public EntityLivingBaseWrapper(int id) {
        super(id);
    }

    public EntityLivingBaseWrapper(Classes c) {
        super(c);
    }
    public int getHurtTime() {
        return ((LivingEntity)obj).hurtTime;
    }
}
