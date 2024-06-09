package fun.inject.inject.wrapper.impl.other;

import fun.inject.inject.wrapper.Wrapper;
import fun.utils.Classes;
import fun.utils.Methods;

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
