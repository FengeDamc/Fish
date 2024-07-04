package com.fun.inject.injection.wrapper.impl.network;

import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.utils.version.clazz.Classes;

public class INetHandler extends Wrapper {
    public INetHandler(Object obj) {
        super(Classes.INetHandler);
        this.obj = obj;
    }
}
