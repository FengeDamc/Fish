package com.fun.inject;



import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.MinecraftType;
import com.fun.inject.inject.MinecraftVersion;
import com.fun.inject.inject.asm.api.Inject;
import com.fun.inject.inject.asm.api.Mixin;
import com.fun.inject.inject.asm.api.Transformer;
import com.fun.inject.inject.asm.api.Transformers;
import com.fun.inject.inject.wrapper.impl.MinecraftWrapper;
import com.fun.utils.Methods;
import com.fun.gui.FishFrame;

import com.fun.network.TCPClient;
import com.fun.network.TCPServer;
import com.fun.utils.font.FontManager;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.HashMap;


public class Agent {
    public static Native instrumentation;

    public static FishFrame fishFrame;
    public static final String VERSION="1.1";
    public static boolean isAgent =false;
    public static ClassTransformer transformer;
    public static MinecraftType minecraftType=MinecraftType.VANILLA;
    public static MinecraftVersion minecraftVersion=MinecraftVersion.VER_189;
    public static HashMap<String,Class<?>> classesMap=new HashMap<>();
    public static final int SERVERPORT=11451;

    public static ClassLoader classLoader=null;
    public static Logger logger= LogManager.getLogger("FunClient");
    public static Class<?> findClass(String name) {

        try {
            return classLoader.loadClass(name.replace('/','.'));
        } catch (ClassNotFoundException e) {

        }
        return null;
    }



    private static void loadJar(URLClassLoader urlClassLoader, URL jar) throws Throwable {
        NativeUtils.loadJar(urlClassLoader, jar);
    }

    public static synchronized void start() throws URISyntaxException, IOException, InterruptedException {
                isAgent =true;
                logger.info("attached!!");

                instrumentation = new Native();
                boolean running = true;
                while (running) {
                    for (Object o : Thread.getAllStackTraces().keySet().toArray()) {
                        Thread thread = (Thread) o;
                        if (thread.getName().equals("Client thread")) {

                            classLoader=thread.getContextClassLoader();
                            running = false;
                            break;
                        }
                    }
                }
                System.out.println(classLoader.getClass().getName());

                try {
                    if (ClassLoader.getSystemClassLoader()!=(classLoader)) {
                        try {
                            loadJar((URLClassLoader) classLoader,Agent.class.getProtectionDomain().getCodeSource().getLocation());

                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        System.out.println("added URL To CL");
                    }
                    Class<?> agentClass = Agent.findClass("com.fun.inject.Agent");


                    for (Method m : agentClass.getDeclaredMethods()) {
                        if (m.getName().equals("init")) {

                            m.invoke(null, classLoader);

                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                Transformers.init();
                transformer = new ClassTransformer();
                instrumentation.addTransformer(transformer, true);


                NativeUtils.setEventNotificationMode(1,54);


                for (Transformer transformer : Transformers.transformers) {
                    try {

                        if(transformer.clazz==null){
                            System.out.println("NULL CLASS");
                            continue;
                        }
                        instrumentation.retransformClasses(transformer.clazz);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                NativeUtils.setEventNotificationMode(0,54);
                instrumentation.doneTransform();
                System.out.println("Transform classes successfully");






    }


    public static void init(ClassLoader cl) {
        classLoader=cl;
        isAgent=true;

        MinecraftWrapper.get().addScheduledTask(() -> {
            FunGhostClient.init();


            ConfigModule.loadConfig();
            TCPClient.send(Main.SERVERPORT,"mcpath "+ System.getProperty("user.dir"));
            TCPServer.startServer(SERVERPORT);
            FontManager.init();

            logger.info("o god start!");
        });
        //logger.info("start init");








    }
    public static class ClassTransformer {

        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            for (Transformer transformer : Transformers.transformers) {
                if (transformer.clazz == classBeingRedefined && loader == classLoader) {
                    transformer.oldBytes = classfileBuffer;

                    try {
                        FileUtils.writeByteArrayToFile(new File(System.getProperty("user.dir"),transformer.getName() + "Old.class"), classfileBuffer);
                    } catch (IOException e) {

                    }
                    ClassNode node = Transformers.node(transformer.oldBytes);

                    for (Method method : transformer.getClass().getDeclaredMethods()) {
                        //Agent.logger.info(method.toString());
                        if (method.isAnnotationPresent(Inject.class)) {
    
                            if (method.getParameterCount() != 1 || !MethodNode.class.isAssignableFrom(method.getParameterTypes()[0]))
                                continue;
    
                            Inject inject = method.getAnnotation(Inject.class);
    
                            String methodToModify = inject.method();
                            String desc = inject.descriptor();
    
                            String obfName = Mappings.getObfMethod(methodToModify);
                            if (obfName == null || obfName.isEmpty()) {
                                logger.error("Could not find {} in class {}", methodToModify, transformer.getName());
                                continue;
                            }


                        // huh???
                            for (MethodNode mNode : node.methods) {
                                //logger.info(mNode.name+mNode.desc);
                                if (mNode.name.equals(obfName) && (Transformers.contains(desc.split(" "), mNode.desc) || desc.isEmpty() || mNode.desc.equals(desc))) {
                                    try {
                                        method.invoke(transformer, mNode);
                                        //logger.info("transformed "+method.getName());
    
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        //logger.error("Failed to invoke method {} {}", e.getMessage(), e.getStackTrace()[0]);
                                        e.printStackTrace();
                                    }
    
                                    break;
                                }
                            }
                        }
                        else if(method.isAnnotationPresent(Mixin.class)){
                            if (method.getParameterCount() != 1 || !MethodNode.class.isAssignableFrom(method.getParameterTypes()[0]))
                                continue;

                            Mixin inject = method.getAnnotation(Mixin.class);
                            Methods methodInfo=inject.method();
                            String name=methodInfo.getName();
                            String desc=methodInfo.getDescriptor();

                            for (MethodNode mNode : node.methods) {
                                //logger.info(mNode.name+mNode.desc);
                                //Agent.logger.info(mNode.name);

                                if (mNode.name.equals(name) && mNode.desc.equals(desc)) {
                                    try {
                                        method.invoke(transformer, mNode);
                                        //logger.info("transformed "+method.getName());

                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        //logger.error("Failed to invoke method {} {}", e.getMessage(), e.getStackTrace()[0]);
                                        e.printStackTrace();
                                    }

                                    break;
                                }
                            }
                        }
                        
                    
                    }
                    byte[] newBytes = Transformers.rewriteClass(node);
                    if(newBytes==null){
                        logger.error(className+" rewriteClass failed");
                        return null;
                    }
                    File fo = new File(System.getProperty("user.dir"),transformer.getName() + ".class");
                    File fdo = new File(System.getProperty("user.dir"),transformer.getName() + "DeObf.class");


                    try {
                        FileUtils.writeByteArrayToFile(fdo,Mappings.deobfClass(newBytes));

                        FileUtils.writeByteArrayToFile(fo, newBytes.clone());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logger.info("transformer:" + transformer.getName() + " bytes in " + fo.getAbsolutePath());

                    transformer.newBytes = newBytes;
                    return transformer.newBytes;
                }
            }
            return null;
        }
    };


}
