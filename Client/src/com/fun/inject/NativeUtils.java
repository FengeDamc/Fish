package com.fun.inject;

import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.ArrayList;


public class NativeUtils {
    public static ArrayList<Agent.ClassTransformer> transformers=new ArrayList<>();
    public static ArrayList<ClassDefiner> classDefiners=new ArrayList<>();

    public static byte[] transform(  ClassLoader         loader,
                                            String              className,
                                            Class<?>            classBeingRedefined,
                                            ProtectionDomain protectionDomain,
                                            byte[]              classfileBuffer) throws IllegalClassFormatException {
        try {
            if (className==null||!Mappings.getUnobfClass(className).startsWith("net/minecraft")) return classfileBuffer;
            for (Agent.ClassTransformer t : transformers) {
                byte[] b = t.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                if (b != null) {
                    classDefiners.add(new ClassDefiner(classBeingRedefined,b));
                    //System.out.println("trans:"+ Mappings.getUnobfClass(className));
                    return classfileBuffer;
                }
            }

            return classfileBuffer;
        }
        catch (Exception e){
            e.printStackTrace();
            return classfileBuffer;
        }
    }
    public static class ClassDefiner{
        public Class<?> clazz;
        public byte[] bytes;

        public ClassDefiner(Class<?> c , byte[] b) {
            clazz=c;bytes=b;
        }
    }
    public static native void setEventNotificationMode(int state,int event);

    public static native ArrayList<Class<?>> getAllLoadedClasses();
    public static native void redefineClass(Class<?> clazz,byte[] bytes);
    public static void retransformClass(Class<?> clazz){
        retransformClass0(clazz);
    }
    public static native void retransformClass0(Class<?> clazz);
    public static native void clickMouse(int event);
    public static native void freeLibrary();
    public static native void loadJar(URLClassLoader cl, URL url);
    public static native void messageBox(String msg,String title);
    public static native void addToSystemClassLoaderSearch(String path);
    public static native Class<?> defineClass(ClassLoader cl,byte[] bytes);



}
