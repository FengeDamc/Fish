package com.fun.inject.injection.wrapper.impl.world;


import com.fun.inject.Mappings;
import com.fun.inject.injection.wrapper.Wrapper;

import java.lang.reflect.Field;

public class Vec3Wrapper extends Wrapper {


    private Object vecObj;
    private double x, y, z;

    public Vec3Wrapper(Object vecObj) {
        super("net/minecraft/util/Vec3");
        this.vecObj = vecObj;

        //field_72448_b,yCoord,2,Y coordinate of Vec3D
        //field_72449_c,zCoord,2,Z coordinate of Vec3D
        //field_72450_a,xCoord,2,X coordinate of Vec3D

        try {
            Field field = getClazz().getDeclaredField(Mappings.getObfField("field_72450_a"));
            field.setAccessible(true);

            x = (Double) field.get(vecObj);

            field = getClazz().getDeclaredField(Mappings.getObfField("field_72448_b"));
            field.setAccessible(true);

            y = (Double) field.get(vecObj);

            field = getClazz().getDeclaredField(Mappings.getObfField("field_72449_c"));
            field.setAccessible(true);

            z = (Double) field.get(vecObj);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public Vec3Wrapper(int x, int y, int z) {
        super("net/minecraft/util/Vec3");
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3Wrapper(double x, double y, double z) {
        super("net/minecraft/util/Vec3");
        this.x =  x;
        this.y =  y;
        this.z =  z;
    }



}
