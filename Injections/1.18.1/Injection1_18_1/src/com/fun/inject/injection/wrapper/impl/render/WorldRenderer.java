package com.fun.inject.injection.wrapper.impl.render;

import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;

public class WorldRenderer extends Wrapper
{
    public BufferBuilder obj;
    public WorldRenderer(BufferBuilder instance) {
        //super(Classes.WorldRenderer);
        obj=instance;
    }

    public WorldRenderer pos(double v, double v1, double v2) {
        return new WorldRenderer((BufferBuilder) obj.vertex(v, v1, v2));
    }

    public void endVertex() {
        obj.endVertex();//Methods.endVertex.invoke(obj);
    }

    public void begin(int i, DefaultVertexFormats postion) {
        switch (i){
            case 1:
                obj.begin(VertexFormat.Mode.LINES,postion.obj);
                break;
            case 7:
                obj.begin(VertexFormat.Mode.QUADS,postion.obj);
                break;//Methods.begin_WorldRenderer.invoke(obj,i,postion.obj);

        }
    }
}
