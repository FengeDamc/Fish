package com.fun.inject.injection.wrapper.impl.render;

import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.fields.Fields;

public class DefaultVertexFormats extends Wrapper {
    public DefaultVertexFormats(Object instance) {
        super(Classes.DefaultVertexFormats);
        obj=instance;
    }
    public static DefaultVertexFormats getPOSTION(){
        return new DefaultVertexFormats(Fields.POSITION_DefaultVertexFormats.get(null));
    }
}
