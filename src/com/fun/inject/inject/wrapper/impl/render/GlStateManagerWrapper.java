package com.fun.inject.inject.wrapper.impl.render;

import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.inject.ReflectionUtils;
import com.fun.inject.inject.wrapper.Wrapper;


public class GlStateManagerWrapper extends Wrapper {
    public static Classes glStateManager=Classes.GlStateManager;
    public GlStateManagerWrapper() {
        super(Classes.GlStateManager);
    }
    public static void color(float f0,float f1,float f2){
        //ReflectionUtils.invokeMethod(glStateManager.getClazz(), Methods.color_GlStateManager.getName(),new Class[]{float.class,float.class,float.class},f0,f1,f2);
        ReflectionUtils.invokeMethod(glStateManager, Methods.color_GlStateManager,f0,f1,f2);

    }
    public static void color(float f0,float f1,float f2,float f3){
        //ReflectionUtils.invokeMethod(glStateManager.getClazz(), Methods.color_GlStateManager.getName(),new Class[]{float.class,float.class,float.class},f0,f1,f2);
        ReflectionUtils.invokeMethod(glStateManager, Methods.color_4f_GlStateManager,f0,f1,f2,f3);

    }
    public static void bindTexture(int i0){
        ReflectionUtils.invokeMethod(glStateManager.getClazz(),Methods.bindTexture_GlStateManager.getName(),new Class[]{int.class},i0);
        ReflectionUtils.invokeMethod(glStateManager,Methods.bindTexture_GlStateManager,i0);
    }

    public static void disableBlend() {
        Methods.disableBlend_GlStateManager.invoke(null);
    }

    public static void enableTexture2D() {
        Methods.enableTexture2D_GlStateManager.invoke(null);
    }

    public static void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
        Methods.tryBlendFuncSeparate_GlStateManager.invoke(null,i,i1,i2,i3);
    }

    public static void disableTexture2D() {
        Methods.disableTexture2D_GlStateManager.invoke(null);
    }

    public static void enableBlend() {
        Methods.enableBlend_GlStateManager.invoke(null);
    }
}
