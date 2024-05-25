package fun.inject;

import java.lang.instrument.UnmodifiableClassException;
import java.util.ArrayList;


public class Native{
    public Class<?> nativeUtils;
    public Native(Class<?> nativeUtilsClazz){
        nativeUtils=nativeUtilsClazz;
    }
    public Native(){
    }
    public void addTransformer(Agent.ClassTransformer transformer, boolean canRetransform) {
        NativeUtils.transformers.add(transformer);
    }


    public void addTransformer(Agent.ClassTransformer transformer) {
        /*try {
            Class<?> nativeUtils=ClassLoader.getSystemClassLoader().loadClass("fun.inject.NativeUtils");
            Field ft= nativeUtils.getDeclaredField("transformers");
            ArrayList<ClassFileTransformer> tList= (ArrayList<ClassFileTransformer>) ft.get(null);
            tList.add(transformer);

        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {

        }*/
        NativeUtils.transformers.add(transformer);
    }




    public void retransformClasses(Class<?>... classes) throws UnmodifiableClassException {
        for(Class<?> kls:classes)
            NativeUtils.retransformClass0(kls);
    }




    public Class<?>[] getAllLoadedClasses() {
        return NativeUtils.getAllLoadedClasses().toArray(new Class[0]);
    }






    public void redefineClass(Class<?> clazz, byte[] bytes) {
        //ReflectionUtils.invokeMethod(nativeUtils,"redefineClass",new Class[]{Class.class,byte[].class},clazz,bytes);
        NativeUtils.redefineClass(clazz,bytes);
    }
    public void doneTransform(){
        ArrayList<NativeUtils.ClassDefiner> classDefiners = NativeUtils.classDefiners;
        for (int i = 0, classDefinersSize = classDefiners.size(); i < classDefinersSize; i++) {
            NativeUtils.ClassDefiner classDefiner = classDefiners.get(i);
            this.redefineClass(classDefiner.clazz, classDefiner.bytes);
        }
        NativeUtils.classDefiners.clear();
    }

    public void removeTransformer(Agent.ClassTransformer transformer) {
        NativeUtils.transformers.remove(transformer);
    }
}
