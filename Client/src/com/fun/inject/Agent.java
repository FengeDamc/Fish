package com.fun.inject;



import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.inject.define.Definer;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Mixin;
import com.fun.inject.injection.asm.api.Transformer;
import com.fun.inject.injection.asm.api.Transformers;
import com.fun.inject.injection.asm.transformers.ClassLoaderTransformer;
import com.fun.inject.mapper.Mapper;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.network.packets.PacketInit;
import com.fun.network.packets.PacketMCPath;
import com.fun.network.packets.PacketMCVer;
import com.fun.utils.version.methods.Methods;
import com.fun.gui.FishFrame;

import com.fun.network.TCPClient;
import com.fun.network.TCPServer;
import com.fun.client.font.FontManager;

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
import java.nio.file.Files;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


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
    public static final String[] selfClasses=new String[]{"com.fun","org.newdawn","javax.vecmath","com.sun.jna"};

    public static ClassLoader classLoader=null;
    public static Logger logger= LogManager.getLogger("FunClient");
    public static Class<?> findClass(String name) throws ClassNotFoundException {
      return classLoader.loadClass(name.replace('/','.'));
    }
    public static final Map<String, byte[]> classes = new HashMap<>();
    private static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1)
            outStream.write(buffer, 0, len);
        outStream.close();
        return outStream.toByteArray();
    }

    public static void cacheJar(File file) throws Exception {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(file.toPath()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null)
                if (!entry.isDirectory())
                    if (entry.getName().endsWith(".class"))
                        classes.put(entry.getName().replace("/", ".").substring(0, entry.getName().length() - 6), readStream(zis));
        }
    }


    public static void injectClassLoader(Class<?> classLoader) {
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
                insnList.add(new VarInsnNode(Opcodes.ALOAD,getArgumentCount(mn.desc)));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(Agent.class),"hookFindClass","(Ljava/lang/ClassLoader;Ljava/lang/String;)Ljava/lang/Class;"));
                insnList.add(new VarInsnNode(Opcodes.ASTORE,2));
                insnList.add(new VarInsnNode(Opcodes.ALOAD,2));
                insnList.add(new JumpInsnNode(Opcodes.IFNULL,l));
                insnList.add(new VarInsnNode(Opcodes.ALOAD,2));
                insnList.add(new InsnNode(Opcodes.ARETURN));
                insnList.add(l);
                mn.instructions.insert(insnList);
            }
            if (mn.name.equals("loadClass")) {
                LabelNode l = new LabelNode();
                for (int i = 0; i < mn.instructions.size(); ++i) {
                    AbstractInsnNode methodInsnNode = mn.instructions.get(i);
                    if (methodInsnNode instanceof MethodInsnNode) {
                        /*if (((MethodInsnNode) methodInsnNode).name.equals("containsKey")) {
                            InsnList insnList = new InsnList();

                            insnList.add(new JumpInsnNode(Opcodes.IFNE, l));
                            insnList.add(new VarInsnNode(Opcodes.ALOAD,1));
                            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(Agent.class), "isSelfClass", "(Ljava/lang/String;)Z"));
                            AbstractInsnNode ifeq = methodInsnNode.getNext();
                            mn.instructions.insert(methodInsnNode, insnList);
                            mn.instructions.insert(ifeq,l);
                            break;
                        }*/

                    }
                    if(methodInsnNode.getOpcode()==Opcodes.GOTO&&methodInsnNode instanceof JumpInsnNode){
                        LabelNode l2 = new LabelNode();
                        l= (LabelNode) methodInsnNode.getNext();
                        InsnList insnList=new InsnList();
                        insnList.add(new VarInsnNode(Opcodes.ALOAD,1));
                        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(Agent.class), "isSelfClass", "(Ljava/lang/String;)Z"));
                        insnList.add(new JumpInsnNode(Opcodes.IFEQ,l2));
                        insnList.add(new VarInsnNode(Opcodes.ALOAD,0));
                        insnList.add(new VarInsnNode(Opcodes.ALOAD,1));
                        insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,Type.getInternalName(Agent.class),"hookFindClass","(Ljava/lang/ClassLoader;Ljava/lang/String;)Ljava/lang/Class;"));
                        insnList.add(new VarInsnNode(Opcodes.ASTORE,4));
                        //todo findClass
                        insnList.add(new JumpInsnNode(Opcodes.GOTO,((JumpInsnNode) methodInsnNode).label));
                        insnList.add(l2);
                        mn.instructions.insert(l,insnList);
                        break;

                    }
                }

            }
        }

        ClassWriter writer=new ClassWriter(ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
        try {
            node.accept(writer);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bytes = writer.toByteArray();
        NativeUtils.redefineClass(classLoaderTransformer.clazz,bytes);
        classLoaderTransformer.newBytes=bytes;
        try {
            FileUtils.writeByteArrayToFile(new File(System.getProperty("user.home"),classLoaderTransformer.name+".class"), bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static int getArgumentCount(String methodDescriptor) {
        int argumentCount = 0;

        for(int currentOffset = 1; methodDescriptor.charAt(currentOffset) != ')'; ++argumentCount) {
            while(methodDescriptor.charAt(currentOffset) == '[') {
                ++currentOffset;
            }

            if (methodDescriptor.charAt(currentOffset++) == 'L') {
                int semiColumnOffset = methodDescriptor.indexOf(59, currentOffset);
                currentOffset = Math.max(currentOffset, semiColumnOffset + 1);
            }
        }

        return argumentCount;
    }

    public static byte[] readClazzBytes(Class<?> c) throws IOException {

        return InjectUtils.getClassBytes(c);//(c.getName().replace('.', '/') + ".class"));
    }
    public static boolean isSelfClass(String name){
        //NativeUtils.messageBox(name,"Fish");
        //System.out.println(name+" hook");
        name=name.replace('/','.');
        for (String cname : selfClasses) {
            if (name.startsWith(cname))
            {
                return true;
            }
        }
        return false;
    }
    public static Class<?> hookFindClass(ClassLoader cl,String name) {
        for (String cname : selfClasses) {
            if (name.replace('/', '.').startsWith(cname))
                try {
                    byte[] bytes = classes.get(name);//InjectUtils.getClassBytes(cl, name);//IOUtils.readAllBytes(ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class"));
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

    public static boolean b;
    public static boolean b1;
    public static void _(){
        if(isAgent||!b){
            System.out.println("jii");
        }
        else if(b1){
            System.out.println("j22");
        }
        else{
            System.out.println("j23");
        }
    }
    public static void getVersion(){
        TCPClient.send(Main.SERVERPORT,new PacketMCVer(null));
        try {
            Class<?> c=Agent.findClass("net.minecraft.client.Minecraft");
            if(c!=null) {
                Agent.minecraftType = MinecraftType.MCP;
                if (ReflectionUtils.invokeMethod(c, minecraftVersion == MinecraftVersion.VER_1181?"m_91087_":
                        "func_71410_x") != null)//m_91087_
                    Agent.minecraftType = MinecraftType.FORGE;
            }

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }



    private static void loadJar(URLClassLoader urlClassLoader, URL jar) {
        NativeUtils.loadJar(urlClassLoader, jar);
    }
    private static void defineClassesInCache(){
        for(String s:classes.keySet()){
            Definer.defineClass(s);
        }
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
                bufferedreader.close();//4efd1f021072263228d944e87c06a543
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
                        //System.out.println(thread.getName()+" "+thread.getContextClassLoader());
                    }
                }
                getVersion();
                System.out.println("jarPath:"+jarPath);
                File injection=new File(new File(jarPath).getParent(),"/injections/"+minecraftVersion.injection);
                //System.out.println(injection.getAbsolutePath());
                injection=Mapper.mapJar(injection,minecraftType);
                NativeUtils.addToSystemClassLoaderSearch(injection.getAbsolutePath());
                System.out.println("injection: "+injection.getAbsolutePath());
                //if(classLoader.getClass().getName().contains("launchwrapper"))injectClassLoader(classLoader.getClass());
                //if(classLoader.getClass().getSuperclass().getName().contains("ModuleClassLoader"))injectClassLoader(classLoader.getClass().getSuperclass());
                //ModuleClassLoader
                try {
                    if (Agent.class.getClassLoader() != (classLoader)) {
                        if(classLoader.getClass().getName().contains("launchwrapper")||classLoader.getClass().getSuperclass().getName().contains("ModuleClassLoader")){
                            cacheJar(injection);
                            cacheJar(new File(jarPath));
                            defineClassesInCache();
                        }
                        else{
                            loadJar((URLClassLoader) classLoader, injection.toURI().toURL());
                            loadJar((URLClassLoader) classLoader, new File(jarPath).toURI().toURL());
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

        Transformers.init();
        transformer = new ClassTransformer();
        instrumentation.addTransformer(transformer, true);

        //NativeUtils.messageBox("native cl:"+NativeUtils.class.getClassLoader(),"Fish");

        for (Transformer transformer : Transformers.transformers) {
            try {

                if(transformer.clazz==null){
                    System.out.println("NULL CLASS");
                    continue;
                }
                NativeUtils.setEventNotificationMode(1,54);
                NativeUtils.retransformClass(transformer.clazz);
                NativeUtils.setEventNotificationMode(0,54);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("2");

        instrumentation.doneTransform();
        System.out.println("3");
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


        TCPClient.send(TCPServer.getTargetPort(),new PacketInit());
        System.out.println("client init start");
        FunGhostClient.init();
        System.out.println("client init successful");
        ConfigModule.loadConfig();
        System.out.println("config loaded");
        TCPClient.send(Main.SERVERPORT, new PacketMCPath(System.getProperty("user.dir")));//"mcpath " +
        TCPServer.startServer(SERVERPORT);
        FontManager.init();
        System.out.println("fish ghost client start!");






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
                        e.printStackTrace();
                    }
                    ClassNode node = Transformers.node(transformer.oldBytes);

                    //System.out.println("1");
                    for (Method method : transformer.getClass().getDeclaredMethods()) {
                        //System.out.println("2");
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

                            System.out.println(obfDesc+" "+obfName);
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


                    try {

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
