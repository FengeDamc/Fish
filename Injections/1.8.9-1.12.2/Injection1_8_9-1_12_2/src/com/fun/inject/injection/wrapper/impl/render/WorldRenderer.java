package com.fun.inject.injection.wrapper.impl.render;

import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;

public class WorldRenderer extends Wrapper
{
    public WorldRenderer(Object instance) {
        super(Classes.WorldRenderer);
        obj=instance;
    }

    public WorldRenderer pos(double v, double v1, double v2) {
        return new WorldRenderer(Methods.pos_WorldRenderer.invoke(obj,v, v1, v2));
    }

    public void endVertex() {
        Methods.endVertex.invoke(obj);
    }

    public void begin(int i, DefaultVertexFormats postion) {
        Methods.begin_WorldRenderer.invoke(obj,i,postion.obj);
    }
}
