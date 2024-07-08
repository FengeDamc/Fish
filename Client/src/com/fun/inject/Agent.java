package com.fun.inject;



import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Mixin;
import com.fun.inject.injection.asm.api.Transformer;
import com.fun.inject.injection.asm.api.Transformers;
import com.fun.inject.injection.asm.transformers.ClassLoaderTransformer;
import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;
import com.fun.inject.mapper.Mapper;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.network.packets.PacketInit;
import com.fun.network.packets.PacketMCPath;
import com.fun.network.packets.PacketMCVer;
import com.fun.utils.version.methods.Methods;
import com.fun.gui.FishFrame;

import com.fun.network.TCPClient;
import com.fun.network.TCPServer;
import com.fun.utils.font.FontManager;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
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
    public static final String VERSION="1.2";
    public static String jarPath;
    public static boolean isAgent =false;
    public static ClassTransformer transformer;
    public static MinecraftType minecraftType=MinecraftType.VANILLA;
    public static MinecraftVersion minecraftVersion=MinecraftVersion.VER_189;
    public static HashMap<String,Class<?>> classesMap=new HashMap<>();
    public static final int SERVERPORT=11451;
    public static final String[] selfClasses=new String[]{"com.fun","org.newdawn","javax.vecmath"};

    public static ClassLoader classLoader=null;
    public static Logger logger= LogManager.getLogger("FunClient");
    public static Class<?> findClass(String name) throws ClassNotFoundException {
      return classLoader.loadClass(name.replace('/','.'));
    }
    public static void injectClassLoader(ClassLoader classLoader) {
        ClassLoaderTransformer classLoaderTransformer=new ClassLoaderTransformer(classLoader);
        Transformers.transformers.add(classLoaderTransformer);
        try {
            classLoaderTransformer.oldBytes=readClazzBytes(classLoaderTransformer.clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //NativeUtils.setEventNotificationMode(1,54);
        //NativeUtils.retransformClass(classLoaderTransformer.clazz);
        //NativeUtils.setEventNotificationMode(0,54);
        //Transformers.transformers.remove(classLoaderTransformer);
        ClassReader cr=new ClassReader(classLoaderTransformer.oldBytes);
        ClassNode node = new ClassNode();
        cr.accept(node, ClassReader.EXPAND_FRAMES);
        for(MethodNode mn:node.methods){
            if(mn.name.equals("findClass")){
                LabelNode l=new LabelNode();
                InsnList insnList=new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD,0));
                insnList.add(new VarInsnNode(Opcodes.ALOAD,1));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(Agent.class),"hookFindClass","(Ljava/lang/ClassLoader;Ljava/lang/String;)Ljava/lang/Class;"));
                insnList.add(new VarInsnNode(Opcodes.ASTORE,2));
                insnList.add(new VarInsnNode(Opcodes.ALOAD,2));
                insnList.add(new JumpInsnNode(Opcodes.IFNULL,l));
                insnList.add(new VarInsnNode(Opcodes.ALOAD,2));
                insnList.add(new InsnNode(Opcodes.ARETURN));
                insnList.add(l);
                mn.instructions.insert(insnList);
            }
        }
        ClassWriter writer=new ClassWriter(ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        byte[] bytes = writer.toByteArray();
        NativeUtils.redefineClass(classLoaderTransformer.clazz,bytes);
        classLoaderTransformer.newBytes=bytes;
        try {
            FileUtils.writeByteArrayToFile(new File(System.getProperty("user.home"),classLoaderTransformer.name + "Old.class"), bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static byte[] readClazzBytes(Class<?> c) throws IOException {

        return InjectUtils.getClassBytes(c);//(c.getName().replace('.', '/') + ".class"));
    }
    public static Class<?> hookFindClass(ClassLoader cl,String name) {
        for (String cname : selfClasses) {
            if (name.replace('/', '.').startsWith(cname))
                try {
                    byte[] bytes = InjectUtils.getClassBytes(cl, name);//IOUtils.readAllBytes(ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class"));
                    return (Class<?>) ReflectionUtils.invokeMethod(
                            cl,
                            "defineClass",
                            new Class[]{
                                    String.class,
                                    byte[].class,
                                    int.class,
                                    int.class
                            },
                            name,
                            bytes,
                            0,
                            bytes.length
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }


        }
        return null;
    }
    public static void getVersion(){
        try {
            Class<?> c=Agent.findClass("net.minecraft.client.Minecraft");
            if(c!=null) {
                Agent.minecraftType = MinecraftType.MCP;
                if (ReflectionUtils.invokeMethod(c,"func_71410_x") != null)
                    Agent.minecraftType = MinecraftType.FORGE;
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }
        TCPClient.send(Main.SERVERPORT,new PacketMCVer(null));
    }



    private static void loadJar(URLClassLoader urlClassLoader, URL jar) throws Throwable {
        NativeUtils.loadJar(urlClassLoader, jar);
    }

    public static synchronized void start() throws URISyntaxException, IOException, InterruptedException {
                //if(true)throw new RuntimeException("hehe");
                isAgent =true;
                System.out.println("attached!!");

                File f=new File(System.getProperty("user.home")+"\\fish.txt");
                BufferedReader bufferedreader = new BufferedReader(new FileReader(f));
                String line="";
                while ((line = bufferedreader.readLine()) != null) {
                    //srg mcp

                    jarPath=line;

                }
                bufferedreader.close();
                instrumentation = new Native();
                boolean running = true;
                while (running) {
                    for (Object o : Thread.getAllStackTraces().keySet().toArray()) {
                        Thread thread = (Thread) o;
                        if (thread.getName().equals("Client thread")||thread.getName().equals("Render thread")) {

                            classLoader=thread.getContextClassLoader();
                            running = false;
                            break;
                        }
                    }
                }
                getVersion();
                System.out.println("jarPath:"+jarPath);
                File injection=new File(new File(jarPath).getParent(),"/injections/"+minecraftVersion.injection);
                System.out.println(injection.getAbsolutePath());
                injection=Mapper.mapJar(injection,minecraftType);
                NativeUtils.addToSystemClassLoaderSearch(injection.getAbsolutePath());
                if(classLoader.getClass().getName().contains("launchwrapper"))injectClassLoader(classLoader);
                try {
                    if (Agent.class.getClassLoader() != (classLoader)) {

                        try {
                            //loadJar((URLClassLoader)Thread.currentThread().getContextClassLoader(),new File(new File(jarPath).getParent(),"/injections/"+minecraftVersion.injection).toURI().toURL());
                            loadJar((URLClassLoader) classLoader, injection.toURI().toURL());
                            loadJar((URLClassLoader) classLoader, new File(jarPath).toURI().toURL());

                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        System.out.println("added URL To CL");
                    }


                    Class<?> agentClass = Agent.findClass("com.fun.inject.Agent");
                    for (Method m : agentClass.getDeclaredMethods()) {
                        if (m.getName().equals("init")) {
                            m.invoke(null, classLoader, jarPath);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //NativeUtils.messageBox("pcl:"+classLoader.getParent(),"Fish");
                //init(classLoader,jarPath);
                transform();



    }

    public static void transform() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try {
            loader.loadClass("com.fun.inject.injection.asm.api.Transformers");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Transformers.init();
        transformer = new ClassTransformer();
        instrumentation.addTransformer(transformer, true);


        NativeUtils.setEventNotificationMode(1,54);
        //NativeUtils.messageBox("native cl:"+NativeUtils.class.getClassLoader(),"Fish");

        for (Transformer transformer : Transformers.transformers) {
            try {

                if(transformer.clazz==null){
                    System.out.println("NULL CLASS");
                    continue;
                }
                //System.out.println("TRANS:"+transformer.clazz.getName());
                NativeUtils.retransformClass(transformer.clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        NativeUtils.setEventNotificationMode(0,54);
        instrumentation.doneTransform();
        System.out.println("Transform classes successfully");

    }

    public static void init(ClassLoader cl, String jarPathIn) {
        classLoader=cl;
        jarPath=jarPathIn;
        isAgent=true;
        getVersion();
        File injection=new File(new File(jarPath).getParent(),"/injections/"+minecraftVersion.injection);
        try {
            Mapper.readMappings(injection.getAbsolutePath(),minecraftType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Class<?> agentClass = Agent.findClass("com.fun.client.FunGhostClient");
        //System.out.println("clientcl:" + agentClass.getClassLoader());
        //Class<?> agentClass = Agent.findClass("com.fun.inject.Agent");
        //System.out.println("agentcl:"+agentClass.getClassLoader());


        MinecraftWrapper.get().addScheduledTask(()->{
            try {
                System.out.println("client init start");
                TCPClient.send(TCPServer.getTargetPort(),new PacketInit());
                FunGhostClient.init();
                System.out.println("client init successful");
                ConfigModule.loadConfig();
                System.out.println("config loaded");
                TCPClient.send(Main.SERVERPORT, new PacketMCPath(System.getProperty("user.dir")));//"mcpath " +
                TCPServer.startServer(SERVERPORT);
                FontManager.init();
                System.out.println("fish ghost client start!");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });






    }
    public static class ClassTransformer {
        public static ClassNode node(byte[] bytes) {
            if (bytes != null && bytes.length != 0) {
                ClassReader reader = new ClassReader(bytes);
                ClassNode node = new ClassNode();
                reader.accept(node, ClassReader.EXPAND_FRAMES);
                return node;
            }

            return null;
        }

        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            for (Transformer transformer : Transformers.transformers) {
                if (transformer.clazz == classBeingRedefined && loader == classLoader) {
                    transformer.oldBytes = classfileBuffer;

                    try {
                        FileUtils.writeByteArrayToFile(new File(System.getProperty("user.home"),transformer.getName() + "Old.class"), classfileBuffer);
                    } catch (IOException e) {

                    }
                    ClassNode node = Transformers.node(transformer.oldBytes);

                    for (Method method : transformer.getClass().getDeclaredMethods()) {
                        //Agent.System.out.println(method.toString());
                        if (method.isAnnotationPresent(Inject.class)) {

                            if (method.getParameterCount() != 1 || !MethodNode.class.isAssignableFrom(method.getParameterTypes()[0]))
                                continue;

                            Inject inject = method.getAnnotation(Inject.class);

                            String methodToModify = inject.method();
                            String desc = inject.descriptor();

                            String obfName = Mapper.getObfMethod(methodToModify,transformer.getName(),desc);
                            String obfDesc=Mapper.getObfMethodDesc(desc);//Mappings.getObfMethod(methodToModify);
                            if (obfName == null || obfName.isEmpty()) {
                                logger.error("Could not find {} in class {}", methodToModify, transformer.getName());
                                continue;
                            }


                        // huh???
                            for (MethodNode mNode : node.methods) {
                                //System.out.println(mNode.name+mNode.desc);
                                if(mNode.name.equals(obfName)&&mNode.desc.equals(obfDesc)){
                                    try {
                                        method.invoke(transformer, mNode);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                        else if(method.isAnnotationPresent(Mixin.class)){
                            if (method.getParameterCount() != 1)
                                continue;

                            Mixin inject = method.getAnnotation(Mixin.class);
                            Methods methodInfo=inject.method();
                            String name=methodInfo.getName();
                            String desc=methodInfo.getDescriptor();
                            boolean trans=false;
                            for (MethodNode mNode : node.methods) {


                                if (mNode.name.equals(name) && mNode.desc.equals(desc)) {
                                    try {
                                        method.invoke(transformer, mNode);

                                        trans=true;
                                        //System.out.println("transformed "+method.getName());

                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        logger.error("Failed to invoke method {} {}", e.getMessage(), e.getStackTrace()[0]);
                                        //e.printStackTrace();
                                    }

                                    break;
                                }
                            }
                            if(!trans){
                                logger.info("method {}{} not trans", name,desc);
                                try {
                                    MethodNode mn=(MethodNode)method.invoke(transformer,node(transformer.oldBytes));
                                    node.methods.add(mn);
                                } catch (Exception e) {
                                    logger.error("Failed to add method {} {}", method.getParameterTypes(), method.getName());

                                }
                            }
                        }


                    }
                    byte[] newBytes = Transformers.rewriteClass(node);
                    if(newBytes==null){
                        logger.error(className+" rewriteClass failed");
                        return null;
                    }
                    File fo = new File(System.getProperty("user.home"),transformer.getName() + ".class");
                    File fdo = new File(System.getProperty("user.home"),transformer.getName() + "DeObf.class");


                    try {
                        FileUtils.writeByteArrayToFile(fdo,Mappings.deobfClass(newBytes));

                        FileUtils.writeByteArrayToFile(fo, newBytes.clone());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("transformer:" + transformer.getName() + " bytes in " + fo.getAbsolutePath());

                    transformer.newBytes = newBytes;
                    return transformer.newBytes;
                }
            }
            return null;
        }
    };


}
