package com.fun.inject;


import com.fun.inject.inject.InjectUtils;
import com.sun.tools.attach.VirtualMachine;
import com.fun.gui.Injector;
import com.fun.network.TCPServer;
import sun.jvmstat.monitor.*;
import sun.jvmstat.perfdata.monitor.PerfStringMonitor;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class Main {
    public static VirtualMachine vm;
    public static String path = System.getProperty("user.dir");
    public static String dllpath = "libagent.dll";
    public static String mcPath=System.getProperty("user.dir");
    public static boolean started=false;
    public static String pid;
    public static final int SERVERPORT=17573;
    public static Injector injector;
    static {
        if(!Agent.isAgent)System.loadLibrary("libinjector");


    }
    public static void start()
    {
            Agent.classLoader=Main.class.getClassLoader();
            TCPServer.startServer(SERVERPORT);
            Method addURL = null;

            try {
                pid = String.valueOf(InjectUtils.getMinecraftProcessId());
            } catch (InterruptedException e) {

            }
            File f=new File(System.getProperty("user.home")+"\\fish.txt");
            if(!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {

                }
            }
            try {
                PrintWriter pw = new PrintWriter(f);
                pw.print(new File(path,"FunGhostClient.jar").getAbsolutePath());
                pw.close();
            } catch (FileNotFoundException e) {

            }
            File dll=new File(path,dllpath);
            InjectorUtils.injectorR(Integer.parseInt(pid),dll.getAbsolutePath());
            //System.out.println("injected in:"+pid);



        }

    public static void main(String[] args) {
        injector=new Injector();
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
