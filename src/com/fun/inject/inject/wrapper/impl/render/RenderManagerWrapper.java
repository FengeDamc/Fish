package com.fun.inject.inject.wrapper.impl.render;


import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.ReflectionUtils;
import com.fun.inject.inject.wrapper.Wrapper;

public class RenderManagerWrapper extends Wrapper {
    private final Object renderManagerObj;

    public RenderManagerWrapper(Object renderManagerObj) {
        super("net/minecraft/client/renderer/entity/RenderManager");
        this.renderManagerObj = renderManagerObj;
    }

    public double getRenderPosX() {
        Object value = ReflectionUtils.getFieldValue(
                renderManagerObj, Mappings.getObfField("field_78725_b"));
        return value == null ? 0.0 : (Double) value;
    }

    public double getRenderPosY() {
        Object value = ReflectionUtils.getFieldValue(
                renderManagerObj, Mappings.getObfField("field_78726_c"));
        return value == null ? 0.0 : (Double) value;
    }

    public double getRenderPosZ() {
        Object value = ReflectionUtils.getFieldValue(
                renderManagerObj, Mappings.getObfField("field_78728_n"));
        return value == null ? 0.0 : (Double) value;
    }
}
