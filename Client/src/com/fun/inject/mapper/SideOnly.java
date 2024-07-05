package com.fun.inject.mapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SideOnly
{
    static enum Type{
        AGENT,
        INJECTOR
    }
    Type value() default Type.AGENT;
}
