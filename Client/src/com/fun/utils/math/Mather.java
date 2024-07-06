package com.fun.utils.math;


import com.fun.utils.math.vecmath.Vec3;

import javax.vecmath.Vector2f;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

public class Mather {
    public static final double SQRT2=Math.sqrt(2);
    public static double jl(Vec3 src, Vec3 target){
        double x = src.xCoord - target.xCoord;
        double z = src.zCoord - target.zCoord;
        double y = src.yCoord - target.yCoord;
        double xx = x*x;
        double zz = z*z;
        double yy = y*y;
        double xyz=Math.sqrt(xx+yy+zz);
        return abs(xyz);
    }

    public static float coerce(float a, float max, float min){

        return Math.min(Math.max(min,a),max);
    }
    public static double randomRange(double minIn,double maxIn){//1,2
        double min=Math.min(minIn,maxIn);
        double max=Math.max(minIn,maxIn);
        PerlinNoise pn=new PerlinNoise();
        return min+pn.next()*(max-min);
    }
    public static double randomGaussianRange(double minIn,double maxIn){//1,2
        double min=Math.min(minIn,maxIn);
        double max=Math.max(minIn,maxIn);

        return min+ ThreadLocalRandom.current().nextGaussian()*(max-min);
    }
    public static double randomRange(double minIn,double maxIn,PerlinNoise pn){//1,2
        double min=Math.min(minIn,maxIn);
        double max=Math.max(minIn,maxIn);
        return min+pn.next()*(max-min);
    }
    public static Vector2f wrapAngleTo180_float(Vector2f old) {
        old.x = MathHelper.wrapAngleTo180_float(old.x);
        old.y = MathHelper.wrapAngleTo180_float(old.y);
        return new Vector2f(old);
    }



    public static float getAngleDifference(final float a, final float b) {
        return Float.parseFloat(Double.valueOf(MathHelper.wrapAngleTo180_double( (a)-b)).toString());//((((a - b) % 360.00F) + 540.00F) % 360.00F) - 180.00F;
    }
    public static Vector2f limitAngleChange( Vector2f currentRotation, Vector2f targetRotation,float turnSpeed) {
        if(turnSpeed<1){
            return new Vector2f(targetRotation);
        }
        final float yawDifference = getAngleDifference(targetRotation.y, currentRotation.y);
        final float pitchDifference = getAngleDifference(targetRotation.x, currentRotation.x);
        Vector2f v=new Vector2f(
                currentRotation.x + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)),
                currentRotation.y + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)));

        return new Vector2f(abs(getAngleDifference(v.x,targetRotation.x))<1?targetRotation.x:v.x,abs(getAngleDifference(v.y,targetRotation.y))<1?targetRotation.y:v.y);
    }



}
