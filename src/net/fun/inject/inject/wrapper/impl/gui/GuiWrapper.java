package net.fun.inject.inject.wrapper.impl.gui;

import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.Wrapper;
import net.fun.utils.Classes;
import net.fun.utils.Methods;

import java.lang.reflect.Method;

public class GuiWrapper extends Wrapper {
    public Object gui=null;
    public GuiWrapper() {
        super(Classes.Gui);
        this.gui= ReflectionUtils.newInstance(getClazz());
    }
    public static void drawRect(int left, int top, int right, int bottom, int color){
        ReflectionUtils.invokeMethod(Classes.Gui, Methods.drawRect_Gui,left,top,right,bottom,color);
    }
}
