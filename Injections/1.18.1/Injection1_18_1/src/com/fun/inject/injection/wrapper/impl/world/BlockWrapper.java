package com.fun.inject.injection.wrapper.impl.world;


import com.fun.inject.Mappings;
import com.fun.inject.injection.wrapper.Wrapper;

import java.lang.reflect.Field;

public class BlockWrapper extends Wrapper {

    private Object blockObj;

    private MaterialWrapper material;

    public BlockWrapper() {
        super("net/minecraft/block/Block");
    }

    public void setBlockObj(Object blockObj) {
        this.blockObj = blockObj;
    }

    public MaterialWrapper getMaterial() {

        try {
            // FD: afh/J net/minecraft/block/Block/field_149764_J
            Field field = getClazz().getDeclaredField(Mappings.getObfField("field_149764_J"));
            field.setAccessible(true);
            material = new MaterialWrapper(field.get(blockObj));
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return material;
    }
}
