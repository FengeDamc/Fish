package com.fun.inject.injection.wrapper.impl.gui;


import com.fun.utils.version.clazz.Classes;
import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;

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
