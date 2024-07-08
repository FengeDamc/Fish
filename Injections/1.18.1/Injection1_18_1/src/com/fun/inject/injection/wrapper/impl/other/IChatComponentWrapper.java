package com.fun.inject.injection.wrapper.impl.other;


import com.fun.inject.injection.wrapper.Wrapper;
import net.minecraft.network.chat.Component;

public class IChatComponentWrapper extends Wrapper {
    public Component obj;
    public IChatComponentWrapper(Component obj) {
        super(Component.class.getName());
        this.obj=obj;
    }
    public String getUnformattedText(){
        return (obj).getContents();
    }
}
