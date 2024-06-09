package fun.inject;


import com.sun.jna.JNIEnv;
import com.sun.tools.attach.VirtualMachine;
import fun.client.FunGhostClient;
import fun.gui.FishFrame;
import fun.inject.inject.InjectUtils;
import fun.network.TCPServer;
import org.lwjgl.Sys;
import sun.instrument.InstrumentationImpl;
import sun.jvmstat.monitor.*;
import sun.jvmstat.perfdata.monitor.AbstractMonitoredVm;
import sun.jvmstat.perfdata.monitor.AbstractPerfDataBuffer;
import sun.jvmstat.perfdata.monitor.PerfDataBufferImpl;
import sun.jvmstat.perfdata.monitor.PerfStringMonitor;
import sun.misc.Unsafe;
import sun.tools.attach.WindowsVirtualMachine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class Main {
    public static VirtualMachine vm;
    public static String path = System.getProperty("user.dir");
    public static String dllpath = "libagent.dll";
    public static String mcPath=System.getProperty("user.dir");
    public static boolean started=false;
    public static final int SERVERPORT=17573;
    static {
        System.loadLibrary("libinjector");


    }
    public static void main(String[] args) {
        Agent.classLoader=Main.class.getClassLoader();
        TCPServer.startServer(SERVERPORT);
        Method addURL = null;
        int pid=0;
        try {
            pid = InjectUtils.getMinecraftProcessId();
        } catch (InterruptedException e) {

        }
        File dll=new File(path,dllpath);

        InjectorUtils.injector(pid,dll.getAbsolutePath());//这边正在抄袭反射dll注入
        System.out.println("injected in:"+pid);



    }

    private static File writeagent(Class<?> class1) {
        try {
            Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0.0");
            manifest.getMainAttributes().putValue("Can-Redefine-Classes", "true");
            manifest.getMainAttributes().putValue("Can-Retransform-Classes", "true");
            manifest.getMainAttributes().putValue("Agent-Class", class1.getName());
            File file = new File(System.getProperty("java.io.tmpdir") + getRandomString(6) + ".jar");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            new JarOutputStream(new FileOutputStream(file), manifest).close();
            file.deleteOnExit();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static class HackMonitor extends  PerfStringMonitor{

        public HackMonitor(String s, Variability variability, boolean b, ByteBuffer byteBuffer) {
            super(s, variability, b, byteBuffer);
        }

        @Override
        public String stringValue() {
            return  "1";
        }
    }
}
