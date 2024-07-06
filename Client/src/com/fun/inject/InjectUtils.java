package com.fun.inject;


import com.fun.inject.injection.asm.api.Transformers;

import com.fun.network.packets.PacketDestroy;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.IntByReference;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.fun.network.TCPClient;
import org.apache.commons.compress.utils.IOUtils;


import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;

public class InjectUtils {

    public static String getSrg(MinecraftVersion ver, MinecraftType type) {
        return "assets/mappings/" + type.getType() + "_" + ver.getVer() + ".srg";
    }
    public static String getCvsM(MinecraftVersion ver) {
        return "assets/mappings/" +"methods" + ver.getVer() + ".csv";
    }
    public static String getCvsF(MinecraftVersion ver) {
        return "assets/mappings/" +"fields" + ver.getVer() + ".csv";
    }

    public static int getMinecraftProcessId() throws InterruptedException {
        User32 user32 = User32.INSTANCE;
        IntByReference pid = new IntByReference(-1);
        do {
            //for(String s:WindowEnumerator.getWindows())
            WinDef.HWND findWindow;
            WinDef.HWND hWnd = findWindow = user32.FindWindow("LWJGL", null);

            if (findWindow != null && findWindow.getPointer() != null) {
                char[] buffer = new char[1024];
                user32.GetWindowText(hWnd, buffer, buffer.length);
                final String windowText = new String(buffer);
                user32.GetWindowThreadProcessId(hWnd, pid);
            } else {
                Thread.sleep(100L);
            }
        } while (pid.getValue() == -1);
        return pid.getValue();
    }
    public static int getMinecraftProcessId2() throws InterruptedException {
        while (true){
            for(VirtualMachineDescriptor virtualMachineDescriptor: VirtualMachine.list()){
                for(MinecraftVersion mcVer:MinecraftVersion.values()){
                    if(virtualMachineDescriptor.displayName().contains("net.minecraft")){
                        return Integer.parseInt(virtualMachineDescriptor.id());
                    }
                }
            }
            Thread.sleep(100L);

        }
    }

    public static void redefineClass(Class<?> clazz, byte[] newByte) throws UnmodifiableClassException, ClassNotFoundException {
        Agent.instrumentation.redefineClass(clazz, newByte);
    }

    public static void destroyClient() {
        if(!Agent.isAgent){
            TCPClient.send(Agent.SERVERPORT,new PacketDestroy());
            return;
        }
        Agent.instrumentation.removeTransformer(Agent.transformer);
        Transformers.transformers.forEach(it -> {
            try {
                InjectUtils.redefineClass(it.getClazz(), it.getOldBytes());
            } catch (UnmodifiableClassException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        try {
            Agent.findClass("com.fun.client.FunGhostClient").getDeclaredMethod("destroyClient").invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();

        }


        //NativeUtils.freeLibrary();
        //InjectorUtils.free();
    }

    public static byte[] getClassBytes(Class<?> c) throws IOException {
        String className = c.getName();
        String classAsPath = className.replace('.', '/') + ".class";
        InputStream stream = c.getClassLoader().getResourceAsStream(classAsPath);
        byte[] bs= stream == null ? null : IOUtils.toByteArray(stream);
        return bs;
    }
    public static byte[] getClassBytes(ClassLoader cl,String name) throws IOException {
        String className = name;
        String classAsPath = className.replace('.', '/') + ".class";
        InputStream stream = cl.getResourceAsStream(classAsPath);
        byte[] bs= stream == null ? null : IOUtils.toByteArray(stream);
        return bs;
    }

}
