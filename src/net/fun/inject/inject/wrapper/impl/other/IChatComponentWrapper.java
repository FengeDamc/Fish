package net.fun.inject.inject.wrapper.impl.other;

import net.fun.inject.inject.wrapper.Wrapper;
import net.fun.utils.Classes;
import net.fun.utils.Methods;

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
