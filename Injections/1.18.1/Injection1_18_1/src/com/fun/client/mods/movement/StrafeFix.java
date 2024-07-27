package com.fun.client.mods.movement;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.mods.VModule;
import com.fun.eventapi.EventTarget;
import com.fun.eventapi.event.events.EventJump;
import com.fun.eventapi.event.events.EventStrafe;
import com.fun.eventapi.event.events.EventTick;
import com.fun.utils.math.MathHelper;

public class StrafeFix extends VModule {
    public StrafeFix() {
        super("StrafeFix", Category.Movement);
    }

    @Override
    public void onStrafe(EventStrafe event) {
        super.onStrafe(event);
        if(FunGhostClient.rotationManager.isActive()&&(event.forward*event.forward+ event.strafe*event.strafe)!=0)
        {
            EventStrafe e=yawToStrafe(FunGhostClient.rotationManager.getRation().y,direction(event.yaw));
            event.strafe=e.strafe*0.98f;
            event.forward=e.forward*0.98f;
            event.yaw=FunGhostClient.rotationManager.getRation().y;
        }

    }

    @Override
    public void onJump(EventJump event) {
        super.onJump(event);
        event.yaw=movementYaw();
    }
    public float movementYaw(){
        float y1= mc.player.getYRot()-FunGhostClient.rotationManager.getRation().y;
        int time= Math.round(y1/45.0f);
        y1=time*45;
        return FunGhostClient.registerManager.vModuleManager.strafeFix.running? FunGhostClient.rotationManager.getRation().y+y1:mc.player.getYRot();
    }

    public float direction(float yaw) {
        float rotationYaw = yaw;

        if (mc.player.input.forwardImpulse < 0) {//moveForward
            rotationYaw += 180;
        }

        float forward = 1;

        if (mc.player.input.forwardImpulse < 0) {
            forward = -0.5F;
        } else if (mc.player.input.forwardImpulse > 0) {
            forward = 0.5F;
        }

        if (mc.player.input.leftImpulse > 0) {
            rotationYaw -= 70 * forward;
        }

        if (mc.player.input.leftImpulse < 0) {
            rotationYaw += 70 * forward;
        }

        return rotationYaw;
    }
    public EventStrafe yawToStrafe(float playerYaw,float moveYaw){
        int angleDiff = (int) ((MathHelper.wrapAngleTo180_float(moveYaw - playerYaw - 22.5f - 135.0f) + 180.0d) / (45.0d));
        EventStrafe event=new EventStrafe();
        switch (angleDiff){
            case 0:
                event.forward=1;
                event.strafe=0;
                break;
            case 1:
                event.forward=1;
                event.strafe=-1;
                break;
            case 2:
                event.forward=0;
                event.strafe=-1;
                break;
            case 3:
                event.forward=-1;
                event.strafe=-1;
                break;
            case 4:
                event.forward=-1;
                event.strafe=0;
                break;
            case 5:
                event.forward=-1;
                event.strafe=1;
                break;
            case 6:
                event.forward=0;
                event.strafe=1;
                break;
            case 7:
                event.forward=1;
                event.strafe=1;
                break;

        }
        if(mc.player.input.shiftKeyDown)event.slow(0.3d);
        return event;
    }


}
