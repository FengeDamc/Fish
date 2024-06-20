package net.fun.inject.inject.wrapper.impl.render;

import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.Wrapper;
import net.fun.utils.Classes;
import net.fun.utils.Methods;


public class GlStateManagerWrapper extends Wrapper {
    public static Classes glStateManager=Classes.GlStateManager;
    public GlStateManagerWrapper() {
        super(Classes.GlStateManager);
    }
    public static void color(float f0,float f1,float f2){
        //ReflectionUtils.invokeMethod(glStateManager.getClazz(), Methods.color_GlStateManager.getName(),new Class[]{float.class,float.class,float.class},f0,f1,f2);
        ReflectionUtils.invokeMethod(glStateManager, Methods.color_GlStateManager,f0,f1,f2);

    }
    public static void bindTexture(int i0){
        ReflectionUtils.invokeMethod(glStateManager.getClazz(),Methods.bindTexture_GlStateManager.getName(),new Class[]{int.class},i0);
        ReflectionUtils.invokeMethod(glStateManager,Methods.bindTexture_GlStateManager,i0);
    }
}
