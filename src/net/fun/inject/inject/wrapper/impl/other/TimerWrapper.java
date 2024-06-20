package net.fun.inject.inject.wrapper.impl.other;


import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.Wrapper;

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
