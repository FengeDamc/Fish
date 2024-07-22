package com.fun.inject.mapper;

import com.fun.inject.Agent;
import com.fun.inject.Mappings;
import com.fun.inject.MinecraftType;
import com.fun.utils.file.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

public class Mapper {
    public static HashMap<String,String> classMap = new HashMap<>();
    public static HashMap<String,String> methodMap = new HashMap<>();
    public static HashMap<String,String> fieldMap = new HashMap<>();
    public static final String[] obfibleClasses=new String[]{"com/mojang","net/minecraft"};
    public static byte[] getAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();

        return buffer.toByteArray();
    }
    public static String getObfClass(Class<?> c){
        return getObfClass(Type.getInternalName(c));
    }

    public static File mapJar(File jarIn,MinecraftType mcType){
        //if(mcType==MinecraftType.NONE)return jarIn;
        File tj=new File(new File(jarIn.getParent()).getParent(),"/injection_"+mcType.getType()+".jar");
        System.out.println(tj.getAbsolutePath());
        try(JarFile jar = new JarFile(jarIn)){
            if(tj.exists()){
                tj.delete();
            }
            tj.createNewFile();
            tj.deleteOnExit();
            //JarFile target=new JarFile(tj);
            JarOutputStream jos=new JarOutputStream(Files.newOutputStream(tj.toPath()));
            readMappings(jarIn.getAbsolutePath(),mcType);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if(entry.getName().endsWith(".class")){
                    InputStream is=jar.getInputStream(entry);
                    byte[] b= mapBytes(getAllBytes(is),mcType);
                    jos.putNextEntry(new ZipEntry(entry.getName()));
                    jos.write(b);
                    jos.closeEntry();

                    //System.out.println(entry.getName());
                }
            }
            //target.close();
            jos.close();
            return tj;

        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static boolean isMethodDesc(String desc){
        return(desc.contains("(")&&desc.contains(")"));
    }
    public static String map(String mcpName,String owner,String desc){
        if(isMethodDesc(desc)){
            return getObfMethod(mcpName, owner, desc);
        }
        return getObfField(mcpName, owner);
    }
    public static byte[] mapBytes(byte[] bytes,MinecraftType mT){

        ClassNode classNode=null;
        String desc=null;
        try {
            classNode = new ClassNode();
            ClassReader classReader = new ClassReader(bytes);
            classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
            ArrayList<MethodNode> removeMethods = new ArrayList<>();
            ArrayList<FieldNode> removeField = new ArrayList<>();
            //System.out.println("start remap: "+classNode.name);
            for (MethodNode methodNode : classNode.methods) {
                if(methodNode.visibleAnnotations!=null){
                    for (AnnotationNode annotationNode : methodNode.visibleAnnotations) {
                        if(annotationNode.desc.equals("Lcom/fun/inject/mapper/SideOnly;")){
                            for(Object object : annotationNode.values){
                                if(object instanceof String[]){
                                    for(String s : (String[])object){
                                        if((s.equals("AGENT")&&!Agent.isAgent)
                                        ||(s.equals("INJECTOR")&&Agent.isAgent)){
                                            removeMethods.add(methodNode);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(mT==MinecraftType.NONE)continue;
                methodNode.name = getObfMethod(methodNode.name, classNode.name, methodNode.desc);
                methodNode.desc = getObfMethodDesc(methodNode.desc);
                for (AbstractInsnNode insnNode : methodNode.instructions) {
                    if (insnNode instanceof MethodInsnNode) {
                        if(isObfibleClass(((MethodInsnNode) insnNode).owner)) {
                            Class<?> owner=Agent.findClass(getObfClass(((MethodInsnNode) insnNode).owner));

                            ((MethodInsnNode) insnNode).name = getObfMethod(((MethodInsnNode) insnNode).name,
                                    owner, ((MethodInsnNode) insnNode).desc);
                            ((MethodInsnNode) insnNode).owner = getObfClass(((MethodInsnNode) insnNode).owner);

                        }
                        ((MethodInsnNode) insnNode).desc = getObfMethodDesc(((MethodInsnNode) insnNode).desc);
                        desc = ((MethodInsnNode) insnNode).desc;
                    }
                    if (insnNode instanceof FieldInsnNode) {
                        if(isObfibleClass(((FieldInsnNode) insnNode).owner)) {
                            ((FieldInsnNode) insnNode).name = getObfField(((FieldInsnNode) insnNode).name, ((FieldInsnNode) insnNode).owner);
                            ((FieldInsnNode) insnNode).owner = getObfClass(((FieldInsnNode) insnNode).owner);
                        }
                        ((FieldInsnNode) insnNode).desc = getObfFieldDesc(((FieldInsnNode) insnNode).desc);

                    }
                    if (insnNode instanceof TypeInsnNode) {
                        ((TypeInsnNode) insnNode).desc = getObfClass(((TypeInsnNode) insnNode).desc);
                    }
                    if(insnNode instanceof LdcInsnNode)
                    {
                        if(((LdcInsnNode) insnNode).cst instanceof Type){
                            ((LdcInsnNode) insnNode).cst=Type.getType(getObfFieldDesc(((Type) ((LdcInsnNode) insnNode).cst).getDescriptor()));
                        }
                       //System.out.println(((LdcInsnNode) insnNode).cst+" "+((LdcInsnNode) insnNode).cst.getClass().getName());

                    }
                    if(insnNode instanceof InvokeDynamicInsnNode){
                        ((InvokeDynamicInsnNode) insnNode).desc=getObfMethodDesc(((InvokeDynamicInsnNode) insnNode).desc);
                        Object[] bsmArgs = ((InvokeDynamicInsnNode) insnNode).bsmArgs;
                        for (int i = 0, bsmArgsLength = bsmArgs.length; i < bsmArgsLength; i++) {
                            Object a = bsmArgs[i];
                            if (a instanceof Type) {
                                Type b =Type.getType(getObfDesc(((Type) a).getDescriptor()));
                                bsmArgs[i]= b;
                                //System.out.println("type:"+b);//bsmArgs[i]=Type.getType(get)
                            }
                            if(a instanceof Handle){
                                Handle b=new Handle(((Handle) a).getTag()
                                        ,getObfClass(((Handle) a).getOwner()),
                                        map(((Handle) a).getName(),((Handle) a).getOwner(),((Handle) a).getDesc()),
                                        getObfDesc(((Handle) a).getDesc()),
                                        ((Handle) a).isInterface());
                                bsmArgs[i]=b;
                                //System.out.println("handler:"+ ((Handle) a).getOwner()+" "+ ((Handle) a).getName()+" "+ ((Handle) a).getDesc());
                            }
                            //todo
                        }
                        //System.out.println("____"+((InvokeDynamicInsnNode) insnNode).name+" "+((InvokeDynamicInsnNode) insnNode).desc);
                    }
                }
            }
            for(FieldNode fieldNode : classNode.fields){
                if(fieldNode.visibleAnnotations!=null){
                    for (AnnotationNode annotationNode : fieldNode.visibleAnnotations) {
                        if(annotationNode.desc.equals("Lcom/fun/inject/mapper/SideOnly;")){
                            for(Object object : annotationNode.values){
                                if(object instanceof String[]){
                                    for(String s : (String[])object){
                                        if((s.equals("AGENT")&&!Agent.isAgent)
                                                ||(s.equals("INJECTOR")&&Agent.isAgent)){
                                            removeField.add(fieldNode);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(mT==MinecraftType.NONE)continue;
                fieldNode.name=getObfField(fieldNode.name,classNode.name);
                fieldNode.desc = getObfFieldDesc(fieldNode.desc);
                //for(){
                  //  fieldNode.signature
                //}
            }
            classNode.methods.removeAll(removeMethods);
            classNode.fields.removeAll(removeField);
            if(mT!=MinecraftType.NONE) {
                classNode.name = getObfClass(classNode.name);
                classNode.superName = getObfClass(classNode.superName);
            }
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(writer);
            //System.out.println(classNode.name);
            return writer.toByteArray();

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(classNode.name+" "+desc+"|");
        }
        return new byte[0];
    }
    public static String getObfClass(String mcpName){
        boolean isArray = mcpName.contains("[]");
        String cn=mcpName.endsWith("[]")?mcpName.substring(0,mcpName.length()-2):mcpName;
        String t=classMap.get(cn);
        return t==null?mcpName:t+(isArray?"[]":"");
    }
    public static String getObfMethod(String mcpName,String owner,String desc){
        String s=getObfMethodOrNull(mcpName, owner, desc);
        return s==null?mcpName:s;
    }
    public static boolean isObfibleClass(String name){
        name=name.replace('.','/');
        for (String cname : obfibleClasses) {
            if (name.startsWith(cname))
            {
                return true;
            }
        }
        return false;
    }
    public static String getObfMethodOrNull(String mcpName,String owner,String desc){
        owner=owner.replace('.','/');
        String str=methodMap.get(owner + "/" + mcpName + " " + desc);
        if(str==null){
            return null;
        }
        String[] s1=str.split(" ")[0].split("/");
        return s1[s1.length-1];
    }
    public static String getObfMethod(String mcpName,Class<?> owner,String desc)
    {
        String s;
        //System.out.println(owner.getName());
        s=getObfMethodOrNull(mcpName, Mappings.getUnobfClass(owner.getName()),desc);
        if(s!=null)return s;
        Class<?> c= owner;

        while (c.getSuperclass()!=null){
            s=getObfMethodOrNull(mcpName,Mappings.getUnobfClass(c.getName()),desc);
            if(s!=null)return s;
            c=c.getSuperclass();
            //System.out.println("1"+c.getName());
        }
        //System.out.println("2"+owner.getName());
        Class<?>[] is=owner.getInterfaces();
        for (Class<?> i : is) {
            s = getObfMethodOrNull(mcpName, Mappings.getUnobfClass(i.getName()), desc);
            if (s != null) return s;
        }
        return mcpName;
    }
    public static String getObfField(String mcpName,String owner){
        String str=fieldMap.get(owner + "/" + mcpName);
        if(str==null){
            return mcpName;
        }

        String[] s=str.split("/");
        String t=s[s.length-1];
        return t==null?mcpName:t;
    }
    public static String getObfDesc(String desc){
        if(isMethodDesc(desc)){
            return getObfMethodDesc(desc);
        }
        return getObfFieldDesc(desc);
    }
    public static String getObfMethodDesc(String desc){
        org.objectweb.asm.Type[] args = org.objectweb.asm.Type.getArgumentTypes(desc);
        StringBuilder sb=new StringBuilder();
        sb.append("(");
        for(org.objectweb.asm.Type t:args){
            if(t.getSort()== org.objectweb.asm.Type.OBJECT){
                sb.append("L");
                sb.append(getObfClass(t.getInternalName()));
                sb.append(";");
            }
            else if(t.getSort()== Type.ARRAY&&t.getElementType().getSort()== org.objectweb.asm.Type.OBJECT){
                sb.append("[L");
                sb.append(getObfClass(t.getElementType().getInternalName()));
                sb.append(";");

            }
            else{
                sb.append(t.getDescriptor());
            }
        }
        sb.append(")");
        org.objectweb.asm.Type t= org.objectweb.asm.Type.getReturnType(desc);
        if(t.getSort()== org.objectweb.asm.Type.OBJECT){
            sb.append("L");
            sb.append(getObfClass(t.getInternalName()));
            sb.append(";");
        }
        else if(t.getSort()== Type.ARRAY&&t.getElementType().getSort()== org.objectweb.asm.Type.OBJECT){
            sb.append("[L");
            sb.append(getObfClass(t.getElementType().getInternalName()));
            sb.append(";");

        }
        else{
            sb.append(t.getDescriptor());
        }
        return sb.toString();
    }
    public static String getObfFieldDesc(String desc){
        StringBuilder sb=new StringBuilder();
        Type t=Type.getType(desc);
        if(t.getSort()== org.objectweb.asm.Type.OBJECT){
            sb.append("L");
            sb.append(getObfClass(t.getInternalName()));
            sb.append(";");
        }
        else if(t.getSort()== Type.ARRAY&&t.getElementType().getSort()== org.objectweb.asm.Type.OBJECT){
            sb.append("[L");
            sb.append(getObfClass(t.getElementType().getInternalName()));
            sb.append(";");

        }
        else{
            sb.append(t.getDescriptor());
        }
        return sb.toString();
    }
    public static void readMappings(String jarPath, MinecraftType mcType) throws IOException {
        try (JarFile jar = new JarFile(jarPath)) {
            InputStream f=null;
            if(mcType == MinecraftType.VANILLA){
                f=IOUtils.getEntryFromJar(jar,"mappings/vanilla.srg");
            }
            if(mcType == MinecraftType.FORGE){
                f=IOUtils.getEntryFromJar(jar,"mappings/forge.srg");
            }
            if(f!=null){
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(f, StandardCharsets.UTF_8));
                String line="";
                while ((line = bufferedreader.readLine()) != null) {
                    String[] parts = line.substring(4)
                            .split(" ");
                    if(line.startsWith("CL")){
                        classMap.put(parts[1], parts[0]);
                        classMap.put(parts[0], parts[1]);
                    }
                    if(line.startsWith("FD")){
                        fieldMap.put(parts[1], parts[0]);
                        fieldMap.put(parts[0], parts[1]);
                    }
                    if(line.startsWith("MD")){
                        methodMap.put(parts[2]+" "+parts[3], parts[0]+" "+parts[1]);
                        methodMap.put(parts[0]+" "+parts[1],parts[2]+" "+parts[3]);
                    }

                }
            }

        }
        catch(IOException e){

        }


    }


}
