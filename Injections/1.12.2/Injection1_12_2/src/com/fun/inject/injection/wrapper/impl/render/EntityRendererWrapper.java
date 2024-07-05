package com.fun.inject.injection.wrapper.impl.render;


import com.fun.inject.Mappings;
import com.fun.inject.injection.wrapper.Wrapper;

import java.lang.reflect.Method;

public class EntityRendererWrapper extends Wrapper {
    private final Object entityRendererObj;

    public EntityRendererWrapper(Object entityRendererObj) {
        super("net/minecraft/client/renderer/EntityRenderer");
        this.entityRendererObj = entityRendererObj;
    }

    public void orientCamera(float partialTicks) {
        try {
            // MD: bfk/f (F)V net/minecraft/client/renderer/EntityRenderer/func_78467_g (F)V
            Method method = getClazz().getDeclaredMethod(Mappings.getObfMethod("func_78467_g"), float.class);
            method.setAccessible(true);
            method.invoke(entityRendererObj, partialTicks);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
