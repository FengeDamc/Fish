package com.fun.inject.injection.asm.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    String method();

    String descriptor() default  "()V";
}
