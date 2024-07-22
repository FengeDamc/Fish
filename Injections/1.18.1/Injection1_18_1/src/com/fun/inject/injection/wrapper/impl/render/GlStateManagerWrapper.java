package com.fun.inject.injection.wrapper.impl.render;

import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.Wrapper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;


public class GlStateManagerWrapper extends Wrapper {
    //public static Classes glStateManager=Classes.GlStateManager;
    public GlStateManagerWrapper() {
        super("com/mojang/blaze3d/systems/RenderSystem");
    }
    public static void color(float f0,float f1,float f2){
        //ReflectionUtils.invokeMethod(glStateManager.getClazz(), Methods.color_GlStateManager.getName(),new Class[]{float.class,float.class,float.class},f0,f1,f2);
        RenderSystem.setShaderColor(f0,f1,f2,1.0f);//ReflectionUtils.invokeMethod(glStateManager, Methods.color_GlStateManager,f0,f1,f2);

    }
    public static void color(float f0,float f1,float f2,float f3){
        //ReflectionUtils.invokeMethod(glStateManager.getClazz(), Methods.color_GlStateManager.getName(),new Class[]{float.class,float.class,float.class},f0,f1,f2);
        RenderSystem.setShaderColor(f0,f1,f2,f3);//ReflectionUtils.invokeMethod(glStateManager, Methods.color_4f_GlStateManager,f0,f1,f2,f3);

    }
    public static void bindTexture(int i0){
        //ReflectionUtils.invokeMethod(glStateManager.getClazz(),Methods.bindTexture_GlStateManager.getName(),new Class[]{int.class},i0);
        RenderSystem.bindTexture(i0);//ReflectionUtils.invokeMethod(glStateManager,Methods.bindTexture_GlStateManager,i0);
    }

    public static void disableBlend() {
        RenderSystem.disableBlend();//Methods.disableBlend_GlStateManager.invoke(null);
    }

    public static void enableTexture2D() {
        RenderSystem.enableTexture();
    }

    public static void tryBlendFuncSeparate(int i, int i1, int i2, int i3) {
        RenderSystem.blendFuncSeparate(i, i1, i2, i3);
    }

    public static void disableTexture2D() {
        RenderSystem.disableTexture();
    }

    public static void enableBlend() {
        RenderSystem.enableBlend();
    }
}
