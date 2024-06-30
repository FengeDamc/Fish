package com.fun.inject.inject.wrapper.impl.setting;


import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.wrapper.Wrapper;
import com.fun.utils.version.fields.Fields;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GameSettingsWrapper extends Wrapper {

    private final Object gameSettingsObj;

    private final Map<String, KeyBindingWrapper> bindings = new HashMap<>();

    public GameSettingsWrapper(Object gameSettingsObj) {
        super("net/minecraft/client/settings/GameSettings");
        this.gameSettingsObj = gameSettingsObj;

        try {
            Field field = getClazz().getField(Mappings.getObfField("field_74324_K"));
            Object[] value = (Object[]) field.get(gameSettingsObj);
            for (Object v : value) {
                KeyBindingWrapper wrapper = new KeyBindingWrapper(v);
                bindings.put(wrapper.getKeyName(), wrapper);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //field_74341_c,mouseSensitivity,0,
    public float geMouseSensitivity(){
        return (float)Fields.mouseSensitivity.get(gameSettingsObj);
    }
    public boolean isInvertMouse(){
        return (boolean) Fields.invertMouse.get(gameSettingsObj);
    }

    public KeyBindingWrapper getKey(String name) {
        return bindings.getOrDefault(name, null);
    }
    public static final String USE="key.use";//-99
    public static final String ATTACK="key.attack";//-100

}
