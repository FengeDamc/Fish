package com.fun.utils.packet;

import com.fun.client.utils.Rotation.Rotation;

import com.fun.eventapi.event.events.EventMotion;
import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;
import com.fun.utils.math.MathHelper;
import com.fun.utils.math.Mather;
import org.lwjgl.util.vector.Vector2f;

import static com.fun.client.mods.combat.AimAssist.getAngleDifference;


public class RotationManager {
    public static Vector2f ration=new Vector2f();
    public static MinecraftWrapper mc=MinecraftWrapper.get();

    public static boolean isSet;
    public static int tick;
    public static int looktime;
    public static int keepYawTick=0;

    public static int keepPitchTick=0;


    public static float speed;
    public static void onUpdate(){
        if (isSet){
            tick=1;
        }
        else {
            tick=0;
        }
        keepPitchTick=Math.max(0,keepPitchTick-1);
        keepYawTick=Math.max(0,keepYawTick-1);



    }
    public static void set(){
        tick=1;
    }
    public static void unSet(){
        tick=1;
    }
    public static Rotation limitAngleChange2(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
        if(Math.abs(yawDifference)<1&&Math.abs(pitchDifference)<1){
            return targetRotation;
        }
        Rotation newRotation= new Rotation(
                currentRotation.getYaw() + (yawDifference > turnSpeed ? (turnSpeed) : Math.max(yawDifference, -turnSpeed)),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed))
        );

        final float yawDifference2 = getAngleDifference(targetRotation.getYaw(), newRotation.getYaw());
        final float pitchDifference2 = getAngleDifference(targetRotation.getPitch(), newRotation.getPitch());
        if(Math.abs(yawDifference2)<1&&Math.abs(pitchDifference2)<1){
            return targetRotation;
        }
        return newRotation;
    }
    public static Rotation limitAngleChange4(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        final float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
        if(Math.abs(yawDifference)<1&&Math.abs(pitchDifference)<1){
            return currentRotation;
        }
        Rotation newRotation= new Rotation(
                currentRotation.getYaw() + (yawDifference > turnSpeed ? (turnSpeed) : Math.max(yawDifference, -turnSpeed)),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed))
        );

        final float yawDifference2 = getAngleDifference(targetRotation.getYaw(), newRotation.getYaw());
        final float pitchDifference2 = getAngleDifference(targetRotation.getPitch(), newRotation.getPitch());
        if(Math.abs(yawDifference2)<1&&Math.abs(pitchDifference2)<1){
            return targetRotation;
        }
        return newRotation;
    }
    //public static Rotation limitAngleChange3(final Rotation newRotation, final Rotation targetRotation) {

    //}
    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation,final float turnSpeed) {
        float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(
                currentRotation.getYaw() +((yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed))),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed))
        );
    }
    public static Rotation limitAngleChange5(final Rotation currentRotation, final Rotation targetRotation,final float turnSpeed,float igron) {
        //float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        //float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(
                updateRotation(currentRotation.getYaw(), targetRotation.getYaw(),turnSpeed,igron),
                updateRotation(currentRotation.getPitch(), targetRotation.getPitch(),turnSpeed,igron)
        );
    }
    public static float updateRotation(float current, float calc, float maxDelta,float minDelta) {
        float f = MathHelper.wrapAngleTo180_float(calc - current);
        if (f > maxDelta) {
            f = maxDelta;
        }

        if (f < -maxDelta) {
            f = -maxDelta;
        }
        if(f<minDelta||f>-minDelta)f=0;

        return current + f;
    }
    public static void onMotion(EventMotion event) {
        event.yaw=getRation().getX();
        event.pitch=getRation().getY();
    }





    public static boolean setting(){
        return tick>0;
    }

    public static void setRation(float x,float y){

        ration.set(keepPitchTick==0?MathHelper.wrapAngleTo180_float(Mather.coerce(x,90,-90)):ration.x,
                keepYawTick==0?MathHelper.wrapAngleTo180_float(Mather.coerce(y,180,-180)):ration.y);
        isSet=true;
        speed=-1;
        set();

    }
    public static void resetKeepTick(){
        keepPitchTick=0;
        keepYawTick=0;
    }
    public static void setRation(float x,float y,int keepPitchTickIn,int keepYawTickIn){

        ration.set(keepPitchTick==0?Mather.coerce(MathHelper.wrapAngleTo180_float(x),90,-90):ration.x,
                keepYawTick==0?Mather.coerce(MathHelper.wrapAngleTo180_float(y),180,-180):ration.y);
        isSet=true;
        speed=-1;
        if(keepPitchTick==0)keepPitchTick=keepPitchTickIn;
        if(keepYawTick==0) keepYawTick=keepYawTickIn;
        set();

    }
    public static void setRation(Vector2f v, int keep_pitch, int keep_yaw){
        setRation(v.x,v.y, keep_pitch, keep_yaw);
    }
    public static void setRation(Vector2f v){
        setRation(v,0,0);
    }
    public static void setRation(float x,float y,float speedIn){
        ration.set(x,y);
        isSet=true;
        speed=speedIn;
    }
    public static Vector2f getRation(){

        return tick>0?ration:new Vector2f(mc.getPlayer().getPitch(),mc.getPlayer().getYaw());
    }

}
