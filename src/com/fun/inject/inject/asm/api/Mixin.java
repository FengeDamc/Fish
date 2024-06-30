package com.fun.inject.inject.asm.api;

import com.fun.utils.version.methods.Methods;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface Mixin {
    Methods method();
}
