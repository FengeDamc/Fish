package fun.inject.inject.wrapper.impl.gui;

import fun.inject.inject.Mappings;
import fun.inject.inject.ReflectionUtils;
import fun.inject.inject.wrapper.Wrapper;

import java.lang.reflect.Method;

public class GuiWrapper extends Wrapper {
    public Object gui=null;
    public GuiWrapper() {
        super("net/minecraft/client/gui/Gui");
        this.gui= ReflectionUtils.newInstance(getClazz());
    }
    public static void drawRect(int left, int top, int right, int bottom, int color){
        try {
            Class c= Class.forName(Mappings.getObfClass("net/minecraft/client/gui/Gui"));
            Method m= c.getDeclaredMethod(Mappings.getObfMethod("func_73734_a"),int.class,int.class,int.class,int.class,int.class);
            m.setAccessible(true);
            m.invoke(null,left,top,right,bottom,color);
            
        } catch (Exception e) {

        }
    }
}
