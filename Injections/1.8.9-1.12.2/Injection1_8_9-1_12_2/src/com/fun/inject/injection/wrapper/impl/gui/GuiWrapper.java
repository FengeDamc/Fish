package com.fun.inject.injection.wrapper.impl.gui;

import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.Wrapper;

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
