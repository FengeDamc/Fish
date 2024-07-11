package com.fun.inject.injection.asm.transformers;

import com.fun.inject.injection.asm.api.Transformer;
import com.mojang.brigadier.Message;

public class ClassLoaderTransformer extends Transformer {
    public ClassLoaderTransformer(ClassLoader classLoader) {
        this(classLoader.getClass());
    }
    public ClassLoaderTransformer(Class<?> classLoader) {
        clazz=classLoader;
        name=clazz.getName();
        obfName=clazz.getName();

    }

}
