package fun.inject;


import com.sun.tools.attach.VirtualMachine;
import fun.inject.inject.InjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.SecureRandom;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class Main {
    public static VirtualMachine vm;
    public static void main(String[] args) {
        String path = writeagent(Agent.class).getAbsolutePath();
        Method addURL = null;
        try {
            addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            addURL.setAccessible(true);


            String sep = System.getProperty("file.separator");
            try {
                addURL.invoke(ClassLoader.getSystemClassLoader(),
                        new URL("file:///"+System.getProperty("user.dir")+"tools.jar"));
                System.out.println("tools added to url!!!");
            } catch (Exception e) {

            }
        } catch (Exception e) {

        }



        try {

            vm = VirtualMachine.attach(String.valueOf(InjectUtils.getMinecraftProcessId()));
            String p1=new File(Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getFile())
                    .getAbsolutePath();

            vm.loadAgent(p1);
            System.out.println("agent加载成功:\n" + new File(Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getFile())
                    .getAbsolutePath());
            System.out.println(vm.id());
            vm.detach();
        } catch (Exception e) {
            System.out.println("agent加载失败\n" + new File(Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getFile())
                    .getAbsolutePath());
            e.printStackTrace();
            if (vm != null) {
                try {
                    vm.detach();
                } catch (IOException ex) {

                }
            }

        }
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
}
