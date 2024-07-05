package com.fun.inject;


import com.fun.inject.injection.asm.FishClassWriter;
import com.fun.inject.injection.asm.api.Transformers;
import com.fun.utils.file.IOUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarFile;

public class Mappings {

    // friendly -> notch (Class)
    public static final Map<String, String> obfClass = new HashMap<>();

    // notch -> friendly (Class)
    public static final Map<String, String> unobfClass = new HashMap<String,String>();

    // searge -> notch (Field)
    public static final Map<String, String> obfFields = new HashMap<>();

    // searge -> notch (Method)
    public static final Map<String, String> obfMethods = new HashMap<>();
    public static final Map<String, String> unObfFields = new HashMap<>();

    // searge -> notch (Method)
    public static final Map<String, String> unObfMethods = new HashMap<>();
    public static final Map<String, String> obfFullFields = new HashMap<>();

    // searge -> notch (Method)
    public static final Map<String, String> obfFullMethods = new HashMap<>();
    public static final Map<String, String> unObfFullFields = new HashMap<>();

    // searge -> notch (Method)
    public static final Map<String, String> unObfFullMethods = new HashMap<>();

    public static final Map<String, String> friendFields = new HashMap<>();

    public static final Map<String, String> friendMethods = new HashMap<>();
    public static final Map<String, String> unFriendFields = new HashMap<>();

    public static final Map<String, String> unFriendMethods = new HashMap<>();
    static {
        if(Agent.isAgent){
            try {
                readMappings(Agent.minecraftVersion,Agent.minecraftType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getObfMethodDesc(String desc){
        Type[] args = Type.getArgumentTypes(desc);
        StringBuilder sb=new StringBuilder();
        sb.append("(");
        for(Type t:args){
            if(t.getSort()==Type.OBJECT){
                sb.append("L");
                sb.append(getObfClass(t.getInternalName()));
                sb.append(";");
            }
            else{
                sb.append(t.getDescriptor());
            }
        }
        sb.append(")");
        Type t=Type.getReturnType(desc);
        if(t.getSort()==Type.OBJECT){
            sb.append("L");
            sb.append(getObfClass(t.getInternalName()));
            sb.append(";");
        }
        else{
            sb.append(t.getDescriptor());
        }
        return sb.toString();
    }
    public static String getUnObfMethodDesc(String desc){
        Type[] args = Type.getArgumentTypes(desc);
        StringBuilder sb=new StringBuilder();
        sb.append("(");
        for(Type t:args){
            if(t.getSort()==Type.OBJECT){
                sb.append("L");
                sb.append(getUnobfClass(t.getInternalName()));
                sb.append(";");
            }
            else{
                sb.append(t.getDescriptor());
            }
        }
        sb.append(")");
        Type t=Type.getReturnType(desc);
        if(t.getSort()==Type.OBJECT){
            sb.append("L");
            sb.append(getUnobfClass(t.getInternalName()));
            sb.append(";");
        }
        else{
            sb.append(t.getDescriptor());
        }
        return sb.toString();
    }



    public static void readMappings(MinecraftVersion mcVer, MinecraftType mcType) throws IOException {
        JarFile jar=new JarFile(Agent.jarPath);
        if (mcType == MinecraftType.FORGE) {
        }
        else if (mcType == MinecraftType.MCP) {
            if (mcVer == MinecraftVersion.VER_1710 || mcVer == MinecraftVersion.VER_189) {
                InputStream f = IOUtils.getEntryFromJar(jar, InjectUtils.getCvsF(mcVer));
                InputStream m = IOUtils.getEntryFromJar(jar,InjectUtils.getCvsM(mcVer));
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(m, StandardCharsets.UTF_8));
                String line="";
                while ((line = bufferedreader.readLine()) != null) {
                    String[] parts = line.split(",");
                    //srg mcp
                    obfMethods.put(parts[0], parts[1]);
                    unObfMethods.put(parts[1], parts[0]);
                }
                bufferedreader.close();

                bufferedreader = new BufferedReader(new InputStreamReader(f, StandardCharsets.UTF_8));
                while ((line = bufferedreader.readLine()) != null) {
                    String[] parts = line.split(",");
                    //srg mcp

                    obfFields.put(parts[0], parts[1]);
                    unObfFields.put(parts[1], parts[0]);
                }
                bufferedreader.close();
                f.close();
                m.close();
            } else {
                InputStream mcp = IOUtils.getEntryFromJar(jar,InjectUtils.getSrg(mcVer, mcType));

                BufferedReader bufferedreader = new BufferedReader(
                        new InputStreamReader(mcp,
                                StandardCharsets.UTF_8));

                String line = "";
                while ((line = bufferedreader.readLine()) != null) {
                    String[] split = line.split(" ");
                    try {
                        if (line.startsWith("\t")) {
                            if (split.length > 2) {//method
                                //0   1    2
                                //mcp desc srg
                                obfMethods.put(split[2],split[0].substring(1));
                                unObfMethods.put(split[0].substring(1),split[2]);


                            } else {
                                //field
                                //0   1
                                //mcp srg
                                obfFields.put(split[1],split[0].substring(1));
                                unObfFields.put(split[0].substring(1),split[1]);

                            }
                        } else {
                            //class
                            //0   1
                            //mcp friendly
                            obfClass.put(split[1], split[0]);
                            unobfClass.put(split[0], split[1]);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                bufferedreader.close();
            }


        }
        else{
            InputStream fileIn = IOUtils.getEntryFromJar(jar,InjectUtils.getSrg(mcVer, mcType));
            InputStream f = IOUtils.getEntryFromJar(jar,InjectUtils.getCvsF(mcVer));
            InputStream m = IOUtils.getEntryFromJar(jar,InjectUtils.getCvsM(mcVer));

            BufferedReader bufferedreader = new BufferedReader(
                    new InputStreamReader(fileIn,
                            StandardCharsets.UTF_8));

            String line = "";
            if (mcVer == MinecraftVersion.VER_189) {

                while ((line = bufferedreader.readLine()) != null) {

                    String[] parts = line.substring(4)
                            .split(" ");
                    String[] parts2 = line
                            .split(" ");


                    if (parts2[0].startsWith("C")) { // class name

                        unobfClass.put(parts[0], parts[1]);
                        obfClass.put(parts[1], parts[0]);
                    } else if (parts2[0].startsWith("F")) { // field name
                        String notch = parts[0].split("/")[1];

                        String[] split = parts[1].split("/");
                        String searge = split[split.length - 1];

                        obfFields.put(searge, notch);
                        unObfFields.put(notch, searge);
                        obfFullFields.put(searge, parts[0]);
                        unObfFullFields.put(parts[0], searge);
                    } else if (parts2[0].startsWith("M")) { // method name

                        String notch = parts[0].split("/")[1];

                        String[] split = parts[2].split("/");
                        String seargeMethod = split[split.length - 1];

                        obfMethods.put(seargeMethod, notch);
                        unObfMethods.put(notch, seargeMethod);
                        obfFullMethods.put(seargeMethod, parts[0]);
                        unObfFullMethods.put(parts[0], seargeMethod);
                    }
                }

                bufferedreader.close();
                bufferedreader = new BufferedReader(new InputStreamReader(f, StandardCharsets.UTF_8));
                while ((line = bufferedreader.readLine()) != null) {
                    String[] parts = line.split(",");
                    friendFields.put(parts[0], parts[1]);
                    unFriendFields.put(parts[1], parts[0]);
                }
                bufferedreader.close();

                bufferedreader = new BufferedReader(new InputStreamReader(m, StandardCharsets.UTF_8));
                while ((line = bufferedreader.readLine()) != null) {
                    String[] parts = line.split(",");
                    friendMethods.put(parts[0], parts[1]);
                    unFriendMethods.put(parts[1], parts[0]);
                }
                bufferedreader.close();
                f.close();
                m.close();
                fileIn.close();
                //Agent.System.out.println(getObfClass("net/minecraft/client/Minecraft"));

            } else {


                while ((line = bufferedreader.readLine()) != null) {
                    String[] split = line.split(" ");
                    try {
                        if (line.startsWith("\t")) {
                            if (split.length > 2) {//method
                                //0   1    2
                                //obf desc srg
                                obfMethods.put(split[2], split[0].substring(1));
                                unObfMethods.put(split[0].substring(1), split[2]);


                            } else {
                                //field
                                //0   1
                                //obf srg
                                obfFields.put(split[1], split[0].substring(1));
                                unObfFields.put(split[0].substring(1), split[1]);

                            }
                        } else {
                            //class
                            //0   1
                            //obf friendly
                            obfClass.put(split[1], split[0]);
                            unobfClass.put(split[0], split[1]);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                bufferedreader.close();
        }
            System.out.println("readmappings");

        //BufferedReader =new BufferedReader(new FileReader());
    }
        jar.close();


    }

    public synchronized static String getObfClass(String friendlyName) {
        return obfClass.get(friendlyName)==null?friendlyName:obfClass.get(friendlyName);
    }
    public static byte[] deobfClass(byte[] bs){
        ClassNode cn= Transformers.node(bs);
        for(MethodNode mn:cn.methods){

            mn.name=Mappings.friendMethods.get(
                    Mappings.unObfFullMethods.get(cn.name+"/"+mn.name))==null?mn.name:Mappings.friendMethods.get(Mappings.unObfFullMethods.get(cn.name+"/"+mn.name));

        }
        for(FieldNode mn:cn.fields){
            mn.name=Mappings.friendFields.get(Mappings.unObfFullFields.get(cn.name+"/"+mn.name))==null?mn.name:Mappings.friendFields.get(Mappings.unObfFullFields.get(cn.name+"/"+mn.name));
        }
        ClassWriter cw=new FishClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cn.accept(cw);
        return cw.toByteArray();
    }

    public static String getUnobfClass(String obfName) {

        return unobfClass.get(obfName)==null?obfName.replace('.', '/'):unobfClass.get(obfName);
    }

    public static String getUnobfClass(Object obfClass) {
        return getUnobfClass(obfClass.getClass().getName().replace(".", "/"));
    }

    public static String getObfField(String searge) {
        return obfFields.get(searge)==null?searge:obfFields.get(searge);
    }

    public static String getObfMethod(String searge) {
        return obfMethods.get(searge)==null?searge:obfMethods.get(searge);
    }
    public static String getObfFieldFromFriendName(String searge) {
        return obfFields.get(unFriendFields.get(searge))!=null?obfFields.get(unFriendFields.get(searge)):searge;
    }

    public static String getObfMethodFromFriendName(String searge) {
        return obfMethods.get(unFriendMethods.get(searge))!=null?obfMethods.get(unFriendMethods.get(searge)):searge;
    }

}
