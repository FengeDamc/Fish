package com.fun.inject.injection.asm.transformers;

import com.fun.inject.injection.asm.api.Transformer;

public class ClassLoaderTransformer extends Transformer {
    public ClassLoaderTransformer(ClassLoader classLoader) {
        clazz=classLoader.getClass();
        name=clazz.getName();
        obfName=clazz.getName();
    }

}
