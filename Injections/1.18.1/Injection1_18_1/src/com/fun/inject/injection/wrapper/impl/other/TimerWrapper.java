package com.fun.inject.injection.wrapper.impl.other;


import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.fields.Fields;

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
    public float getRenderPartialTicks(){
        return (float) Fields.renderPartialTicks.get(timerObj);
    }

}
