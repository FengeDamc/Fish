package fun.utils;

import fun.inject.Agent;
import fun.inject.inject.Mappings;
import fun.inject.inject.MinecraftType;
import fun.inject.inject.MinecraftVersion;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.util.Map;

public class VMethod extends Version{
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
    public Method getMethod(Classes clazz){
        if(this.method==null){
            for(Method m:clazz.getClazz().getDeclaredMethods()){
                if(Type.getMethodDescriptor(m).equals(this.getDescriptor())){
                    this.method=m;
                    break;
                }
            }
        }
        return this.method;
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
