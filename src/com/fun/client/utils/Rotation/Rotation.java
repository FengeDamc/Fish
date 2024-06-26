package com.fun.client.utils.Rotation;


import com.fun.utils.MathHelper;
import com.fun.inject.inject.wrapper.impl.MinecraftWrapper;
import javax.vecmath.Vector2f;

public class Rotation {
    public static MinecraftWrapper mc=MinecraftWrapper.get();
    public float yaw,pitch;
    public boolean tag=false;
    public Rotation(float yawIn,float pitchIn){
        this.yaw= yawIn>180||yawIn<180? MathHelper.wrapAngleTo180_float(yawIn):yawIn;
        this.pitch=pitchIn>90||pitchIn<-90?MathHelper.wrapAngleTo180_float(pitchIn):pitchIn;
    }

    @Override
    public String toString() {
        return "pitch:"+pitch+" yaw:"+yaw;
    }

    public Rotation(Vector2f v){
        this(v.y,v.x);
    }


    public Vector2f toVec2f(){
        return new Vector2f(pitch,yaw);
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj)||(obj instanceof Rotation&&((Rotation) obj).toVec2f().equals(this.toVec2f()))||(obj instanceof Vector2f&&obj.equals(this.toVec2f()));
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
    public static Rotation player(){
        return new Rotation(mc.getPlayer().getYaw(),mc.getPlayer().getPitch());
    }
}
