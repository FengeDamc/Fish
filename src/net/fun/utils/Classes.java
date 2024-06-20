package net.fun.utils;

import net.fun.inject.Agent;
import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.MinecraftType;
import net.fun.inject.inject.MinecraftVersion;

import java.util.HashMap;

public enum Classes {
    S12PACKET_ENTITY_VELOCITY(new VClass("net/minecraft/network/play/server/S12PacketEntityVelocity").version(MinecraftType.VANILLA,MinecraftVersion.VER_189)
    ,new VClass("net/minecraft/network/play/server/SPacketEntityVelocity").version(MinecraftType.VANILLA,MinecraftVersion.VER_1122),
            new VClass("net/minecraft/network/play/server/SEntityVelocityPacket").version(MinecraftVersion.VER_1165)),
    EntityPlayerSP(new VClass("net/minecraft/client/entity/EntityPlayerSP"),
            new VClass("net/minecraft/client/entity/player/ClientPlayerEntity").version(MinecraftVersion.VER_1165)),
    EntityPlayer(new VClass("net/minecraft/entity/player/EntityPlayer")),
    Entity(new VClass("net/minecraft/entity/Entity")),
    BlockPos(new VClass("net/minecraft/util/math/BlockPos").version(MinecraftVersion.VER_1122),
            new VClass("net/minecraft/util/BlockPos"),
            new VClass("net/minecraft/util/math/BlockPos").version(MinecraftVersion.VER_1165)),
    GlStateManager(new VClass("net/minecraft/client/renderer/GlStateManager")),
    Gui(new VClass("net/minecraft/client/gui/Gui")),
    KeyBinding(new VClass("net/minecraft/client/settings/KeyBinding")),
    Minecraft(new VClass("net/minecraft/client/Minecraft")),
    ScaledResolution(new VClass("net/minecraft/client/gui/ScaledResolution")),
    ChatComponentStyle(new VClass("net/minecraft/util/ChatComponentStyle"),
            new VClass("net/minecraft/util/text/TextComponentBase").version(MinecraftVersion.VER_1122)),
    NetHandlerPlayClient(new VClass("net/minecraft/client/network/NetHandlerPlayClient")),
    EntityLivingBase(new VClass("net/minecraft/entity/EntityLivingBase")),
    Team(new VClass("net/minecraft/scoreboard/Team")),
    IChatComponent(new VClass("net/minecraft/util/IChatComponent"),
            new VClass("net/minecraft/util/text/ITextComponent").version(MinecraftVersion.VER_1122)),
    NetworkPlayerInfo(new VClass("net/minecraft/client/network/NetworkPlayerInfo")),
    MovementInput(new VClass("net/minecraft/util/MovementInput")),
    ChatComponentText(new VClass("net/minecraft/util/ChatComponentText").version(MinecraftVersion.VER_189),
            new VClass("net/minecraft/util/text/TextComponentString"));//net/minecraft/util/ChatComponentText
    public String obf_name ="";
    public String friendly_name ="";
    public Class<?> clazz =null;
    public HashMap<MinecraftType, HashMap<MinecraftVersion,VClass>> map=new HashMap<>();


    Classes(String name){
        this.obf_name =Mappings.getObfClass(name);
        this.friendly_name=name;
        this.clazz = Agent.findClass(this.obf_name);


    }
    Classes(VClass... vClasses){
        for(MinecraftType type: MinecraftType.values()){
            map.put(type,new HashMap<>());
        }
        for(VClass vClass:vClasses){
            map.get(vClass.minecraftType).put(vClass.minecraftVersion,vClass);
        }
    }
    public String getName(){
        VClass V1=map.get(Agent.minecraftType).get(Agent.minecraftVersion);
        if(V1!=null)return V1.obf_name;
        VClass V2=map.get(MinecraftType.VANILLA).get(Agent.minecraftVersion);
        if(V2!=null)return V2.obf_name;
        VClass V3=map.get(MinecraftType.VANILLA).get(MinecraftVersion.VER_189);
        if(V3!=null)return V3.obf_name;
        return null;
    }
    public Class<?> getClazz(){
        VClass V1=map.get(Agent.minecraftType).get(Agent.minecraftVersion);
        if(V1!=null)return V1.clazz;
        VClass V2=map.get(MinecraftType.VANILLA).get(Agent.minecraftVersion);
        if(V2!=null)return V2.clazz;
        VClass V3=map.get(MinecraftType.VANILLA).get(MinecraftVersion.VER_189);
        if(V3!=null)return V3.clazz;
        return null;
    }
    public VClass getVClass(){
        VClass V1=map.get(Agent.minecraftType).get(Agent.minecraftVersion);
        if(V1!=null)return V1;
        VClass V2=map.get(MinecraftType.VANILLA).get(Agent.minecraftVersion);
        if(V2!=null)return V2;
        return map.get(MinecraftType.VANILLA).get(MinecraftVersion.VER_189);
    }

    public boolean isInstanceof(Object instance){
        Class<?> c = instance.getClass();
        while (c != null) {

            if(c==this.getClazz())
                return true;
            c = c.getSuperclass();
        }
        return false;
    }
}
