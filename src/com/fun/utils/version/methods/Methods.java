package com.fun.utils.version.methods;

import com.fun.inject.Agent;
import com.fun.inject.inject.MinecraftType;
import com.fun.inject.inject.MinecraftVersion;
import com.fun.inject.inject.ReflectionUtils;
import com.fun.utils.version.clazz.Classes;

import java.lang.reflect.Method;
import java.util.HashMap;

public enum Methods {
    getEntityID_Entity(new VMethod("func_145782_y","()I")),
    setSprinting(new VMethod("func_70031_b","(Z)V")),
    onUpdate_Entity(new VMethod("func_70071_h_","()V"),
            new VMethod("func_70071_h_","()V")),
    onUpdateWalking_SP(new VMethod("func_175161_p","()V"),
            new VMethod("func_175161_p","()V").version(MinecraftVersion.VER_1165)),
    onLivingUpdate_SP(Classes.EntityPlayerSP,new VMethod("func_70636_d","()V"),
            new VMethod("func_70636_d","()V").version(MinecraftVersion.VER_1165)),
    isUsing(new VMethod("func_71039_bw","()Z"),
            new VMethod("func_184587_cr","()Z").version(MinecraftVersion.VER_1122),
            new VMethod("func_184587_cr","()Z").version(MinecraftVersion.VER_1165)
            ),
    runTick_Minecraft(new VMethod("func_71407_l","()V")),
    color_GlStateManager(new VMethod("func_179124_c","(FFF)V")),
    bindTexture_GlStateManager(new VMethod("func_179144_i","(I)V")),
    drawRect_Gui(new VMethod("func_73734_a","(IIIII)V")),
    setKeyBindState_KeyBinding(Classes.KeyBinding,new VMethod("func_74510_a","(IZ)V")),
    onTick_KeyBinding(Classes.KeyBinding,new VMethod("func_74507_a","(I)V")),
    getKeyCode_KeyBinding(Classes.KeyBinding,new VMethod("func_151463_i","()I")),
    getDisplayName_EntityPlayer(Classes.EntityPlayer,new VMethod("func_145748_c_","()Lnet/minecraft/util/IChatComponent;"),
            new VMethod("func_145748_c_","()Lnet/minecraft/util/text/ITextComponent;").version(MinecraftVersion.VER_1122)),
    getUnformattedText(new VMethod("func_150260_c","()Ljava/lang/String;")),
    getPlayerInfo_String_NetHandlerPlayClient(Classes.NetHandlerPlayClient,new VMethod("func_175104_a","(Ljava/lang/String;)Lnet/minecraft/client/network/NetworkPlayerInfo;")),
    getPlayerInfo_UUID_NetHandlerPlayClient(Classes.NetHandlerPlayClient,new VMethod("func_175102_a","(Ljava/util/UUID;)Lnet/minecraft/client/network/NetworkPlayerInfo;")),
    getUniqueID(Classes.Entity,new VMethod("func_110124_au","()Ljava/util/UUID;")),
    isInvisible(Classes.Entity,new VMethod("func_82150_aj","()Z")),
    getTeam(Classes.EntityLivingBase,new VMethod("func_96124_cp","()Lnet/minecraft/scoreboard/Team;")),
    isSameTeam(Classes.Team,new VMethod("func_142054_a","(Lnet/minecraft/scoreboard/Team;)Z")),
    isSneaking_Entity(Classes.Entity,new VMethod("func_70093_af","()Z")),
    isSneaking_EntityPlayerSP(Classes.EntityPlayerSP,new VMethod("func_70093_af","()Z")),
    getNetHandler_Minecraft(new VMethod("func_147114_u")),
    moveFlying_Entity(new VMethod("func_70060_a","(FFF)V")
            ,new VMethod("func_191958_b","(FFFF)V").version(MinecraftVersion.VER_1122)),
    needNewCheck(new VMethod("needNewCheck","()Z")),
    isButtonDown(Classes.Mousse,new VMethod("isButtonDown","(I)Z")),
    glPushMatrix(Classes.GL11,new VMethod("glPushMatrix","()V")),
    setAngles_Entity(Classes.Entity,new VMethod("func_70082_c","(FF)V")),
    channelRead0NoEvent_NetworkManager(new VMethod("channelRead0NoEvent","(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V")),
    sendPacket_NetworkManager(new VMethod("func_179290_a","(Lnet/minecraft/network/Packet;)V")),
    channelRead0_NetworkManager(new VMethod("channelRead0","(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V")),
    //channelRead0_OBJ_NetworkManager(new VMethod("channelRead0","(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V")),
    processPacket_Packet(new VMethod("func_148833_a","(Lnet/minecraft/network/INetHandler;)V")),
    isSprinting_Entity(new VMethod("func_70051_ag","()Z"));//func_148833_a (Lnet/minecraft/network/INetHandler;)V
//func_70082_c (FF)V
//channelRead0 (Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V


    public String obf_name ="";
    public String friendly_name ="";
    public Method method;
    public Classes parent=null;
    public Class<?>[] args;
    public HashMap<MinecraftType, HashMap<MinecraftVersion,VMethod>> map=new HashMap<>();

    /*Methods(String mname,Class<?> parent,Class<?>... args){
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
    }*/
    public Object invoke(Object target,Object... args){
        Classes parent=getParent();
        if(target==null){
            return ReflectionUtils.invokeMethod(getParent(),this,args);
        }
        else if(parent!=null){
            return ReflectionUtils.invokeMethod(target,getParent(),this,args);
        }
        else {
            return ReflectionUtils.invokeMethod(target,this,args);

        }
    }
    public Object invoke(Object target){
        Classes parent=getParent();
        if(target==null){
            return ReflectionUtils.invokeMethod(parent,this,(Object[])null);
        }
        else if(parent!=null){
            return ReflectionUtils.invokeMethod(target,parent,this,(Object[])null);
        }
        else {
            return ReflectionUtils.invokeMethod(target,this,(Object[])null);

        }
    }
    public Classes getParent(){
        return parent;
    }
    Methods(VMethod... vMethods){
        for(MinecraftType type: MinecraftType.values()){
            map.put(type,new HashMap<>());
        }
        for(VMethod vMethod:vMethods){
            map.get(vMethod.minecraftType).put(vMethod.minecraftVersion,vMethod);
        }
    }
    Methods(Classes parent,VMethod... vMethods){
        this.parent=parent;
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
    /*Methods(String mname,Class<?> parent){
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
        return getVMethod().getDescriptor();
    }

}
