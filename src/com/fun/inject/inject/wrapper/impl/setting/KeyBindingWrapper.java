package com.fun.inject.inject.wrapper.impl.setting;


import com.fun.utils.Methods;
import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.wrapper.Wrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class KeyBindingWrapper extends Wrapper {
    public final Object keyBindingObj;

    private String keyDescription;

    public KeyBindingWrapper(Object keyBindingObj) {
        super("net/minecraft/client/settings/KeyBinding");
        this.keyBindingObj = keyBindingObj;
    }
    public int getKeyCode(){
        return (int) Methods.getKeyCode_KeyBinding.invoke(keyBindingObj);
    }

    public String getKeyName() {
        if (keyDescription == null) {
            // MD: avb/g ()Ljava/lang/String; net/minecraft/client/settings/KeyBinding/func_151464_g ()Ljava/lang/String;

            try {
                Method method = getClazz().getMethod(Mappings.getObfMethod("func_151464_g"));
                Object value = method.invoke(keyBindingObj);
                return value == null ? null : (String) value;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return keyDescription;
    }

    public void setPressed(boolean pressed) {
        // FD: avb/h net/minecraft/client/settings/KeyBinding/field_74513_e

        try {
            Field field = getClazz().getDeclaredField(Mappings.getObfField("field_74513_e"));
            field.setAccessible(true);
            field.set(keyBindingObj, pressed);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public boolean isPressed() {
        // FD: avb/h net/minecraft/client/settings/KeyBinding/field_74513_e

        try {
            Field field = getClazz().getDeclaredField(Mappings.getObfField("field_74513_e"));
            field.setAccessible(true);
            Object value = field.get(keyBindingObj);
            field.setAccessible(false);

            return value != null && (Boolean) value;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return false;
    }
}
