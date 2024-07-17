package com.fun.inject.injection.wrapper.impl.render;

import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.fields.Fields;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;

public class DefaultVertexFormats extends Wrapper {
    public VertexFormat obj;
    public DefaultVertexFormats(VertexFormat instance) {
        super(Classes.DefaultVertexFormats);
        obj=instance;
    }
    public static DefaultVertexFormats getPOSTION(){
        return new DefaultVertexFormats(DefaultVertexFormat.POSITION);
    }
}
