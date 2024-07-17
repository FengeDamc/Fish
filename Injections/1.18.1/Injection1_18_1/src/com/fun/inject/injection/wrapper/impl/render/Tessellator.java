package com.fun.inject.injection.wrapper.impl.render;

import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class Tessellator extends Wrapper {
    public Tesselator obj;
    public Tessellator(Tesselator instance) {
        //super(Classes.Tessellator);
        obj=instance;
    }
    public static Tessellator getInstance() {
        return new Tessellator(Tesselator.getInstance());
    }
    public WorldRenderer getWorldRenderer(){
        return new WorldRenderer(obj.getBuilder());
    }

    public void draw() {
        obj.end();//Methods.draw_Tessellator.invoke(obj);
    }
}
