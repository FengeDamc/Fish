package com.fun.inject.inject.wrapper.impl.world;


import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.wrapper.Wrapper;

import java.lang.reflect.Field;

public class MaterialWrapper extends Wrapper {
    private final Object materialObj;

    public MaterialWrapper(Object materialObj) {
        super("net/minecraft/block/material/Material");
        this.materialObj = materialObj;
    }

    public boolean isReplaceable() {
        // FD: arm/K net/minecraft/block/material/Material/field_76239_H

        try {
            Field field = getClazz().getDeclaredField(Mappings.getObfField("field_76239_H"));
            field.setAccessible(true);
            Object value = field.get(materialObj);
            return value != null && (Boolean) value;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return false;
    }
}
