package com.fun.inject.inject.asm.transformers;

import com.fun.inject.inject.asm.api.Mixin;
import com.fun.inject.inject.asm.api.Transformer;

public class ClassLoaderTransformer extends Transformer {
    public ClassLoaderTransformer(ClassLoader classLoader) {
        clazz=classLoader.getClass();
        name=clazz.getName();
        obfName=clazz.getName();
    }

}
