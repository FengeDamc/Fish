package com.fun.eventapi.event.events;


import com.fun.inject.inject.wrapper.impl.MinecraftWrapper;

public class EventStrafe extends Event {
    public float forward,strafe, friction;
    public  float yaw;
    public float factor=Float.NaN;
    public EventStrafe(float forward, float strafe, float yaw, float friction) {
        super();
        this.forward=forward;
        this.strafe=strafe;
        this.yaw=yaw;
        this.friction = friction;
    }
    public EventStrafe(){

    }
    public void slow(double slowIn){
        this.forward*= (float) slowIn;
        this.strafe*= (float) slowIn;
    }

    public void setSpeed(final double speed, final double motionMultiplier) {
        friction =((float) (forward != 0 && strafe != 0 ? speed * 0.98F : speed));
        MinecraftWrapper mc=MinecraftWrapper.get();
        mc.getPlayer().setMotionX(mc.getPlayer().getMotionX()*motionMultiplier);
        mc.getPlayer().setMotionZ(mc.getPlayer().getMotionZ()*motionMultiplier);

       // mc.thePlayer.motionZ *= motionMultiplier;

    }
}
