package com.fun.inject.inject.wrapper.impl.other;


import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.ReflectionUtils;
import com.fun.inject.inject.wrapper.Wrapper;

public class TimerWrapper extends Wrapper {
    private final Object timerObj;

    public TimerWrapper(Object obj) {
        super("net/minecraft/util/Timer");
        timerObj = obj;
    }

    public float getTimerSpeed() {
        return (Float) ReflectionUtils.getFieldValue(timerObj, Mappings.getObfField("field_74278_d"));
    }

    public void setTimerSpeed(Float speed) {
        ReflectionUtils.setFieldValue(timerObj, Mappings.getObfField("field_74278_d"), speed);
    }

}
