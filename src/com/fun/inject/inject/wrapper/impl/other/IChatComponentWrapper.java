package com.fun.inject.inject.wrapper.impl.other;

import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.inject.wrapper.Wrapper;

public class IChatComponentWrapper extends Wrapper {
    public Object obj;
    public IChatComponentWrapper(Object obj) {
        super(Classes.IChatComponent);
        this.obj=obj;
    }
    public String getUnformattedText(){
        return (String) Methods.getUnformattedText.invoke(obj);
    }
}
