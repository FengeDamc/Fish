package com.fun.inject.inject.wrapper.impl.util;

import com.fun.inject.inject.wrapper.Wrapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.fields.Fields;

public class MouseHelperWrapper extends Wrapper {
    public MouseHelperWrapper(Object target) {
        super(Classes.MouseHelper);
        this.obj=target;
    }
    public int getDeltaY(){
        return (int) Fields.deltaY.get(obj);
    }
    public int getDeltaX(){
        return (int) Fields.deltaX.get(obj);
    }
}
