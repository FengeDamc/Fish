package fun.utils;

import fun.inject.Agent;
import fun.inject.inject.Mappings;
import fun.inject.inject.MinecraftType;
import fun.inject.inject.MinecraftVersion;
import org.objectweb.asm.Type;
import net.minecraft.client.entity.player.ClientPlayerEntity;

import java.lang.reflect.Method;
import java.util.HashMap;

public enum Methods {
    getEntityID_Entity(new VMethod("func_145782_y","()I")),
    setSprinting(new VMethod("func_70031_b","(B)V")),
    onUpdate_Entity(new VMethod("func_70071_h_","()V"),
            new VMethod("func_70071_h_","()V")),
    onUpdateWalking_SP(new VMethod("func_175161_p","()V"),
            new VMethod("func_175161_p","()V").version(MinecraftVersion.VER_1165)),
    onLivingUpdate_SP(new VMethod("func_70636_d","()V"),
            new VMethod("func_70636_d","()V").version(MinecraftVersion.VER_1165)),
    isUsing(new VMethod("func_71039_bw","()Z"),
            new VMethod("func_184587_cr","()Z").version(MinecraftVersion.VER_1122),
            new VMethod("func_184587_cr","()Z").version(MinecraftVersion.VER_1165)
            );
    public String obf_name ="";
    public String friendly_name ="";
    public Method method;
    public Class<?> parent;
    public Class<?>[] args;
    public HashMap<MinecraftType, HashMap<MinecraftVersion,VMethod>> map=new HashMap<>();

    Methods(String mname,Class<?> parent,Class<?>... args){
        m0(mname, parent.getName(), args);
    }
    Methods(String mname){
        obf_name=Mappings.getObfMethod(mname);
        friendly_name=mname;

    }
    public void m0(String mname,String parentName,Class<?>... args){
        try {
            parent= Agent.findClass(Mappings.getObfClass(parentName));
            obf_name=Mappings.getObfMethod(mname);
            method= parent.getMethod(obf_name,args);
            friendly_name=mname;
            this.args=args;
        } catch (ClassNotFoundException | NoSuchMethodException e) {

        }
    }
    Methods(VMethod... vMethods){
        for(MinecraftType type: MinecraftType.values()){
            map.put(type,new HashMap<>());
        }
        for(VMethod vMethod:vMethods){
            map.get(vMethod.minecraftType).put(vMethod.minecraftVersion,vMethod);
        }
    }
    public String getName(){
        VMethod V1=map.get(Agent.minecraftType).get(Agent.minecraftVersion);
        if(V1!=null)return V1.obf_name;
        VMethod V2=map.get(MinecraftType.VANILLA).get(Agent.minecraftVersion);
        if(V2!=null)return V2.obf_name;
        VMethod V3=map.get(MinecraftType.VANILLA).get(MinecraftVersion.VER_189);
        if(V3!=null)return V3.obf_name;
        return null;
    }
    public VMethod getVMethod(){
        VMethod V1=map.get(Agent.minecraftType).get(Agent.minecraftVersion);
        if(V1!=null)return V1;
        VMethod V2=map.get(MinecraftType.VANILLA).get(Agent.minecraftVersion);
        if(V2!=null)return V2;
        VMethod V3=map.get(MinecraftType.VANILLA).get(MinecraftVersion.VER_189);
        if(V3!=null)return V3;
        return null;
    }
    Methods(String mname,Class<?> parent){
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
    }
    public String getDescriptor(){
        return Mappings.getObfMethodDesc(getVMethod().getDescriptor());
    }

}
