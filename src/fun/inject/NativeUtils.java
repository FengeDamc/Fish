package fun.inject;

import fun.inject.inject.Mappings;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import static fun.inject.Agent.logger;


public class NativeUtils {
    public static ArrayList<Agent.ClassTransformer> transformers=new ArrayList<>();
    public static ArrayList<ClassDefiner> classDefiners=new ArrayList<>();

    public static byte[] transform(  ClassLoader         loader,
                                            String              className,
                                            Class<?>            classBeingRedefined,
                                            ProtectionDomain protectionDomain,
                                            byte[]              classfileBuffer) throws IllegalClassFormatException {
        //logger.info("trans:"+ Mappings.getUnobfClass(className));
        try {
            if (!Mappings.getUnobfClass(className).startsWith("net/minecraft")) return classfileBuffer;
            for (Agent.ClassTransformer t : transformers) {
                byte[] b = t.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                if (b != null) {
                    classDefiners.add(new ClassDefiner(classBeingRedefined,b));
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



}
