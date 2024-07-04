package com.fun.utils.version.methods;

import com.fun.inject.Mappings;
import com.fun.inject.MinecraftType;
import com.fun.inject.MinecraftVersion;
import com.fun.utils.version.Version;
import com.fun.utils.version.clazz.Classes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

public class VMethod extends Version {
    public String obf_name ="";
    public String friendly_name ="";
    public Method method=null;
    public Class<?> parent;
    public Class<?>[] args;
    public String desc="()V";
    public String obf_desc="()V";
    /*public VMethod(String mname, Class<?> parent, Class<?>... args){
        m0(mname, parent.getName(), args);
    }
    */
    public VMethod(String mname,String desc){
        obf_name= Mappings.getObfMethod(mname);
        friendly_name=mname;
        this.desc=desc;
        this.obf_desc= Mappings.getObfMethodDesc(desc);
    }
    public VMethod(String mname){
        this(mname,"()V");
    }
    /*public void m0(String mname,String parentName,Class<?>... args){
        try {
            parent= Agent.findClass(Mappings.getObfClass(parentName));
            obf_name=Mappings.getObfMethod(mname);
            method= parent.getMethod(obf_name,args);
            friendly_name=mname;
            this.args=args;
        } catch (ClassNotFoundException | NoSuchMethodException e) {

        }
    }
    public VMethod(String mname, Class<?> parent){
        try {
            m1(mname,parent.getName());
            if(method==null)throw new NoSuchMethodException(mname);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();

        }
    }
    public void m1(String mname,String parentName) throws ClassNotFoundException {
        parent= Agent.findClass(Mappings.getObfClass(parentName));
        obf_name=Mappings.getObfMethod(mname);
        //method= parent.getMethod(obf_name,args);
        for(Method m:parent.getMethods()){
            if(m.getName().equals(obf_name)){
                method=m;
                break;
            }
        }
        friendly_name=mname;
    }*/
    public String getDescriptor(){
        if(method==null)return obf_desc;
        return Type.getType(method).getDescriptor();
    }
    public Method getMethod(Class<?> clazz){
        if(this.method==null){
            this.method=findMethod(clazz);
        }
        return this.method;
    }
    public Method getMethod(Classes c){
        return getMethod(c.getClazz());
    }
    public Method findMethod(Class<?> clazz){
        Class<?> c = clazz;
        while (c != null) {
            if (this.method == null) {
                for (Method m : c.getDeclaredMethods()) {
                    if (Type.getMethodDescriptor(m).equals(this.getDescriptor())) {
                        return m;

                    }
                }
            }
            else return this.method;
            c = c.getSuperclass();


        }
        return null;
    }





    public VMethod version(MinecraftVersion version, MinecraftType type) {
        minecraftVersion=version;
        minecraftType=type;
        return this;
        //VersionNameManager.map.get(minecraftType).put(version,name);
    }
    public VMethod version(MinecraftVersion version) {
        version(version,MinecraftType.VANILLA);
        return this;
    }


    public VMethod version() {
        version(MinecraftVersion.VER_189);
        return this;
    }
}
