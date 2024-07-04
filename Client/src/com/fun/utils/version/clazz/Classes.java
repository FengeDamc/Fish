package com.fun.utils.version.clazz;

import com.fun.inject.Agent;
import com.fun.inject.Mappings;
import com.fun.inject.MinecraftType;
import com.fun.inject.MinecraftVersion;

import java.util.HashMap;

public enum Classes {
    //net.minecraft.network.play.server.SPacketEntityVelocity
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
            new VClass("net/minecraft/util/text/TextComponentString").version(MinecraftVersion.VER_1122)),
    Mousse(new VClass("org/lwjgl/input/Mouse")),
    GL11(new VClass("org/lwjgl/opengl/GL11")),
    MouseHelper(new VClass("net/minecraft/util/MouseHelper")),
    S14PacketEntity(new VClass("net.minecraft.network.play.server.S14PacketEntity"),
            new VClass("net.minecraft.network.play.server.SPacketEntity").version(MinecraftVersion.VER_1122)),
    INetHandler(new VClass("net/minecraft/network/INetHandler")),
    Tessellator(new VClass("net/minecraft/client/renderer/Tessellator")),
    WorldRenderer(new VClass("net/minecraft/client/renderer/WorldRenderer"),
            new VClass("net/minecraft/client/renderer/BufferBuilder").version(MinecraftVersion.VER_1122)),
    DefaultVertexFormats(new VClass("net/minecraft/client/renderer/vertex/DefaultVertexFormats")),
    S18PacketEntityTeleport(new VClass("net/minecraft/network/play/server/S18PacketEntityTeleport"),
            new VClass("net/minecraft/network/play/server/SPacketEntityTeleport").version(MinecraftVersion.VER_1122));/*
    net/minecraft/network/play/server/S18PacketEntityTeleport
    net/minecraft/client/renderer/vertex/DefaultVertexFormats
    net/minecraft/client/renderer/BufferBuilder
    net/minecraft/client/renderer/WorldRenderer
    net/minecraft/client/renderer/Tessellator
    net/minecraft/network/INetHandler
    net/minecraft/util/Timer net/minecraft/util/Timer
	elapsedPartialTicks field_194148_c
	elapsedTicks field_74280_b
	lastSyncSysClock field_74277_g
	renderPartialTicks field_194147_b
	tickLength field_194149_e
	updateTimer ()V func_74275_a
	net.minecraft.network.INetHandler
    */
    //com.netease.mc.mod.network.common.GameState
    public String obf_name ="";
    public String friendly_name ="";
    public Class<?> clazz =null;
    public HashMap<MinecraftType, HashMap<MinecraftVersion,VClass>> map=new HashMap<>();


    Classes(String name){
        this.obf_name =Mappings.getObfClass(name);
        this.friendly_name=name;
        try {
            this.clazz = Agent.findClass(this.obf_name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
    Classes(VClass... vClasses){
        for(MinecraftType type: MinecraftType.values()){
            map.put(type,new HashMap<>());
        }
        for(VClass vClass:vClasses){
            map.get(vClass.minecraftType).put(vClass.minecraftVersion,vClass);
        }
        if(getVClass().clazz==null){
            Agent.logger.info("cant find class:{}",getVClass().obf_name);

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
        //Agent.logger.info(instance.getClass().getName());
        return false;
    }
}
