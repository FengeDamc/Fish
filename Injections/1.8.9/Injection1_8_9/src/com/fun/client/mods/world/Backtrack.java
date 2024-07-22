package com.fun.client.mods.world;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.VModule;
import com.fun.client.settings.Setting;
import com.fun.eventapi.event.events.EventPacket;
import com.fun.eventapi.event.events.EventTick;
import com.fun.inject.injection.wrapper.impl.entity.EntityWrapper;
import com.fun.inject.injection.wrapper.impl.network.packets.PacketWrapper;
import com.fun.inject.injection.wrapper.impl.network.packets.server.S12PacketEntityVelocityWrapper;
import com.fun.inject.mapper.SideOnly;
import com.fun.utils.packet.PacketUtils;
import com.fun.utils.version.clazz.Classes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.S14PacketEntity;

import java.util.ArrayList;
import com.fun.client.mods.Category;

public class Backtrack extends VModule {
    public Backtrack() {
        super("BackTrack",Category.World);
    }
    public Setting lag=new Setting("Lag",this,4,0,20,true);
    public double realX,realY,realZ;
    public int tick=0;

    public static class PacketLater{
        public long time;
        public PacketWrapper packet;

        public PacketLater(PacketWrapper vel) {
            super();
            time=0;//System.nanoTime();
            packet = vel;
        }
    }
    public static ArrayList<PacketLater> packets = new ArrayList<>();

    @Override
    @SideOnly(SideOnly.Type.AGENT)
    public void onTick(EventTick event) {
        super.onTick(event);
        ArrayList<PacketLater> repackets = new ArrayList<>();
        for(PacketLater packet : packets){//1000000
            packet.time++;
            if(packet.time>lag.getValDouble()){
                repackets.add(packet);
                PacketUtils.receivePacketNoEvent(packet.packet);
                System.out.println(packet.time+" "+packets.size());
            }
        }
        packets.removeAll(repackets);
    }

    @Override
    @SideOnly(SideOnly.Type.AGENT)
    public void onPacket(EventPacket packet) {
        super.onPacket(packet);
        //System.out.println("onpacket:"+ Mappings.getUnobfClass(packet.packet.getClass().getName()));
        if(packet.packet instanceof S14PacketEntity){
            //System.out.println("onpacket1");
            if(((S14PacketEntity) packet.packet).getEntity(mc.theWorld).getEntityId()== FunGhostClient.registerManager.vModuleManager.target.target.getEntityID()){
                //System.out.println("onpacket2");
                Entity entity=mc.theWorld.getEntityByID(((S14PacketEntity) packet.packet).getEntity(mc.theWorld).getEntityId());
                if(entity instanceof EntityLivingBase){
                    packets.add(new PacketLater(new PacketWrapper(Classes.S14PacketEntity,packet.packet)));
                    System.out.println("addbacktrack s14");
                    packet.cancel=true;

                }

            }
        }
        if(Classes.S12PACKET_ENTITY_VELOCITY.isInstanceof(packet.packet)){
            //System.out.println("onpacket1");
            S12PacketEntityVelocityWrapper packetEntityVelocity = new S12PacketEntityVelocityWrapper(packet.packet);
            if(packetEntityVelocity.getEntityID()== FunGhostClient.registerManager.vModuleManager.target.target.getEntityID()){
                //System.out.println("onpacket2");
                EntityWrapper entity =new EntityWrapper(packetEntityVelocity.getEntityID());
                if(Classes.EntityLivingBase.isInstanceof(entity.obj)){
                    packets.add(new PacketLater(packetEntityVelocity));
                    //System.out.println("addbacktrack");
                    packet.cancel=true;

                }

            }
        }


    }
}
