package com.fun.client.mods.combat;


import com.fun.client.FunGhostClient;
import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.client.settings.Setting;
import com.fun.client.utils.Rotation.Rotation;
import com.fun.eventapi.event.events.EventMotion;
import com.fun.eventapi.event.events.EventStrafe;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.utils.rotation.RotationUtils;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import org.lwjgl.system.CallbackI;

import javax.vecmath.Vector2f;

public class KillAura extends VModule
{
    public KillAura() {
        super("KillAura",Category.Combat);
    }
    public Vector2f targetRotation=new Vector2f();
    public Vector2f currentRotation=new Vector2f();
    public Setting rotationSpeed=new Setting("RotationSpeed",this,90,0,180,false);
    public Setting rotationSpeed2=new Setting("RotationSpeed2",this,90,0,180,false);

    public Setting slientMove=new Setting("SlientMove",this,true);
    public Setting keepSprint=new Setting("KeepSprint",this,false);
    public Setting CPS = new Setting("CPS", this, 12, 0, 20, true);

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);
        if(FunGhostClient.registerManager.vModuleManager.scaffold.isRunning())return;
        Entity target= FunGhostClient.registerManager.vModuleManager.target.target;
        double speed=rotationSpeed.getValDouble()+(rotationSpeed2.getValDouble()-rotationSpeed.getValDouble())*Math.random();
        if(target!=null){

            targetRotation=AimBot.aim(mc.player.getEyePosition(),target.getEyePosition());
        }
        else {
            targetRotation=new Vector2f(mc.player.getXRot(),mc.player.getYRot());
            speed=180;
        }
        currentRotation= RotationUtils.limitAngleChange(new Rotation(currentRotation),new Rotation(targetRotation), (float) speed).toVec2f();

        FunGhostClient.rotationManager.setRation(currentRotation);
        if(mc.hitResult instanceof EntityHitResult&&Math.random() <CPS.getValDouble()/20){
            mc.gameMode.attack(mc.player,((EntityHitResult) mc.hitResult).getEntity());
            mc.player.swing(InteractionHand.MAIN_HAND);
        }
        if(!keepSprint.getValBoolean())mc.player.setSprinting(false);
    }

    @Override
    public void onStrafe(EventStrafe event) {
        super.onStrafe(event);
        if(FunGhostClient.registerManager.vModuleManager.scaffold.isRunning())return;
        if(!slientMove.getValBoolean()){
            event.yaw=FunGhostClient.rotationManager.getRation().y;
            event.strafe=mc.player.input.leftImpulse*0.98f;
            event.forward=mc.player.input.forwardImpulse*0.98f;
        }
    }
}
