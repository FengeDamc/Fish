package com.fun.inject.injection.dynamic;



import com.fun.inject.injection.dynamic.impl.gui.GuiScreenDynamic;
import com.fun.inject.utils.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

public class Dynamics {

    private static final List<Dynamic> dynamicClasses = new ArrayList<>();

    public static void defineClasses() throws Exception {
        dynamicClasses.forEach(clazz -> {
            try {
                byte[] bytes = clazz.dump();
                ReflectionUtils.invokeMethod(
                        ClassLoader.getSystemClassLoader(),
                        "defineClass",
                        new Class[]{
                                String.class,
                                byte[].class,
                                int.class,
                                int.class
                        },
                        clazz.getName(),
                        bytes,
                        0,
                        bytes.length
                );
                //Lepton.System.out.println("Successful define dynamic class: {}", clazz.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    static {
        dynamicClasses.add(new GuiScreenDynamic());
    }
}
