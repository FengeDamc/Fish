package com.fun.inject.inject.wrapper.impl.render;

import com.fun.inject.inject.wrapper.Wrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;

public class Tessellator extends Wrapper {
    public Tessellator(Object instance) {
        super(Classes.Tessellator);
        obj=instance;
    }
    public static Tessellator getInstance() {
        return new Tessellator(Methods.getInstance_Tessellator.invoke(null));
    }
    public WorldRenderer getWorldRenderer(){
        return new WorldRenderer(Methods.getWorldRenderer_Tessellator.invoke(obj));
    }

    public void draw() {
        Methods.draw_Tessellator.invoke(obj);
    }
}
