package com.fun.inject.injection.wrapper.impl.render;


import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.Wrapper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FontRendererWrapper extends Wrapper {
    private final Font fontRendererObj;

    private int heightCache;

    public FontRendererWrapper(Font obj) {
        //super("net/minecraft/client/gui/FontRenderer");

        this.fontRendererObj = obj;
    }

    public void drawString(String s, int x, int y, int color) {
        ReflectionUtils.invokeMethod(fontRendererObj,Mappings.getObfMethod("func_175063_a"),new Class[]{String.class, int.class, int.class, int.class},s,x,y,color);
        //MD: avn/a (Ljava/lang/String;FFI)I net/minecraft/client/gui/FontRenderer/func_175063_a (Ljava/lang/String;FFI)I
        //MD: avn/a (Ljava/lang/String;FFIZ)I net/minecraft/client/gui/FontRenderer/func_175065_a (Ljava/lang/String;FFIZ)I

    }

    public void drawStringShadow(String s, float x, float y, int color,boolean shadow) {
        ReflectionUtils.invokeMethod(fontRendererObj,Mappings.getObfMethod("func_175065_a"),new Class[]{String.class, float.class, float.class, int.class,boolean.class},s,x,y,color,shadow);

    }

    public int getStringWidth(String s) {
        try {
            String notch = Mappings.getObfMethod("func_78256_a"); // getStringWidth
            if (notch == null || notch.isEmpty()) return 0;
            Method m = getClazz().getDeclaredMethod(notch, String.class);
            Object value = m.invoke(fontRendererObj, s);
            return value == null ? 0 : (Integer) value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getHeight() {
        if (heightCache == 0) {
            try {
                String notch = Mappings.getObfField("field_78288_b"); // FONT_HEIGHT
                if (notch == null || notch.isEmpty()) return 0;
                Field f = getClazz().getDeclaredField(notch);
                Object value = f.get(fontRendererObj);
                heightCache = value == null ? 0 : (Integer) value;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return heightCache;
    }
}
