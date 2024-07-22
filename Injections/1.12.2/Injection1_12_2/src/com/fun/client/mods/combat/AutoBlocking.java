package com.fun.client.mods.combat;

import com.fun.client.FunGhostClient;
import com.fun.client.settings.Setting;
import com.fun.eventapi.event.events.EventTick;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.inject.injection.wrapper.impl.entity.EntityPlayerSPWrapper;
import com.fun.inject.injection.wrapper.impl.entity.EntityWrapper;
import com.fun.inject.injection.wrapper.impl.setting.GameSettingsWrapper;
import com.fun.utils.math.vecmath.Vec3;

import javax.vecmath.Vector2f;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
public class AutoBlocking extends Module {
    public AutoBlocking() {
        super("AutoBlocking",Category.Combat);

    }
    public boolean block=false;

    public Setting blockHurtTime=new Setting("BlockHurtTime",this,5,0,10,true);
    public Setting maxBlockRange =new Setting("MaxBlockRange",this,2,0,6,false)
    {
        @Override
        public void setValDouble(double in) {
            if(in<minBlockRange.getValDouble())in=minBlockRange.getValDouble();
            super.setValDouble(in);
        }
    };


    public Setting minBlockRange =new Setting("MinBlockRange",this,2,0,6,false){
        @Override
        public void setValDouble(double in) {
            if(in>maxBlockRange.getValDouble())in=maxBlockRange.getValDouble();
            super.setValDouble(in);
        }
    };

    public Setting fov=new Setting("FOV",this,80,0,180,true);

    @Override
    public void onTick(EventTick event) {
        super.onTick(event);
        mc.getGameSettings().getKey(GameSettingsWrapper.USE).setPressed(block|mc.getGameSettings().getKey(GameSettingsWrapper.USE).isPressed());


    }

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);

        EntityWrapper target= FunGhostClient.registerManager.vModuleManager.target.target;
        if(target==null||FunGhostClient.registerManager.vModuleManager.target.dist< minBlockRange.getValDouble()||FunGhostClient.registerManager.vModuleManager.target.dist>maxBlockRange.getValDouble()) {
            block=false;
            return;
        }
        EntityPlayerSPWrapper playersp=mc.getPlayer();
        Vector2f v= AimAssist.aim(new Vec3(target.getX(),target.getY()+ target.getEyeHeight(),target.getZ()),
                new Vec3(playersp.getX(), playersp.getY()+ playersp.getEyeHeight(), playersp.getZ()));

        if(Math.abs(v.y-target.getYaw())<fov.getValDouble()) block=mc.getPlayer().getHurtTime()< blockHurtTime.getValDouble();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        block=false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        block=false;

    }
}
