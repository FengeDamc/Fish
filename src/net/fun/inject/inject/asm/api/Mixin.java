package net.fun.inject.inject.asm.api;

import net.fun.utils.Methods;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface Mixin {
    Methods method();
}
