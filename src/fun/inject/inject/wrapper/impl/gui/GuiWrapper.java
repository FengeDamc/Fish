package fun.inject.inject.wrapper.impl.gui;

import fun.inject.inject.Mappings;
import fun.inject.inject.ReflectionUtils;
import fun.inject.inject.wrapper.Wrapper;
import fun.utils.Classes;
import fun.utils.Methods;

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
