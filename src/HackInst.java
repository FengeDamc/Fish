import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.jar.JarFile;

public class HackInst{

    public native void addTransformer(ClassFileTransformer transformer, boolean canRetransform);

    public native void addTransformer(ClassFileTransformer transformer);

    public native boolean removeTransformer(ClassFileTransformer transformer);

    public native boolean isRetransformClassesSupported();

    public native void retransformClasses(Class<?>... classes) throws UnmodifiableClassException;

    public native boolean isRedefineClassesSupported();

    public native void redefineClasses(ClassDefinition... definitions) throws ClassNotFoundException, UnmodifiableClassException;



    public native boolean isModifiableClass(Class<?> theClass) ;

    public native Class[] getAllLoadedClasses() ;

    public native Class[] getInitiatedClasses(ClassLoader loader);

    public native long getObjectSize(Object objectToSize);

    public native void appendToBootstrapClassLoaderSearch(JarFile jarfile);

    public native void appendToSystemClassLoaderSearch(JarFile jarfile);

    public native boolean isNativeMethodPrefixSupported();

    public native void setNativeMethodPrefix(ClassFileTransformer transformer, String prefix);
    static {
        System.loadLibrary("funinst");
    }
}
