package com.fun.inject.inject.wrapper.impl.gui;


import com.fun.utils.Classes;
import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.ReflectionUtils;
import com.fun.inject.inject.wrapper.Wrapper;
import com.fun.inject.inject.wrapper.impl.MinecraftWrapper;

public class ScaledResolutionWrapper extends Wrapper {
    private Object scaledResolutionObj;

    public ScaledResolutionWrapper(MinecraftWrapper mc) {
        super(Classes.ScaledResolution);
        scaledResolutionObj = ReflectionUtils.newInstance(getClazz(), new Class[]{Classes.Minecraft.getClazz()}, mc.getMinecraftObj());
    }

    public int getScaleFactor() {
        return (Integer) ReflectionUtils.invokeMethod(scaledResolutionObj, Mappings.getObfMethod("func_78325_e"));
    }

    public int getScaledWidth() {
        return (Integer) ReflectionUtils.invokeMethod(scaledResolutionObj, Mappings.getObfMethod("func_78326_a"));
    }

    public int getScaledHeight() {
        return (Integer) ReflectionUtils.invokeMethod(scaledResolutionObj, Mappings.getObfMethod("func_78328_b"));
    }

    public double getScaledWidth_double() {
        return (Integer) ReflectionUtils.invokeMethod(scaledResolutionObj, Mappings.getObfMethod("func_78327_c"));
    }

    public double getScaledHeight_double() {
        return (Integer) ReflectionUtils.invokeMethod(scaledResolutionObj, Mappings.getObfMethod("func_78324_d"));
    }

}
