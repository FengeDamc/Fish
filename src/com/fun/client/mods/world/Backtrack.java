package com.fun.client.mods.world;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.eventapi.event.events.EventPacket;
import com.fun.eventapi.event.events.EventTick;
import com.fun.inject.inject.wrapper.impl.entity.EntityWrapper;
import com.fun.inject.inject.wrapper.impl.network.packets.PacketWrapper;
import com.fun.inject.inject.wrapper.impl.network.packets.server.S12PacketEntityVelocity;
import com.fun.inject.inject.wrapper.impl.network.packets.server.S14EntityPacket;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.packet.PacketUtils;

import java.util.ArrayList;

public class Backtrack extends Module {
    public Backtrack() {
        super("BackTrack",Category.World);
    }
    public Setting lag=new Setting("Lag",this,4,0,20,true);
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
    public void onPacket(EventPacket packet) {
        super.onPacket(packet);
        //System.out.println("onpacket:"+ Mappings.getUnobfClass(packet.packet.getClass().getName()));
        if(Classes.S14PacketEntity.isInstanceof(packet.packet)){
            //System.out.println("onpacket1");
            S14EntityPacket packetEntityVelocity = new S14EntityPacket(packet.packet);
            if(packetEntityVelocity.getEntityID()== FunGhostClient.moduleManager.target.target.getEntityID()){
                //System.out.println("onpacket2");
                EntityWrapper entity =new EntityWrapper(packetEntityVelocity.getEntityID());
                if(Classes.EntityLivingBase.isInstanceof(entity.obj)){
                    packets.add(new PacketLater(packetEntityVelocity));
                    //System.out.println("addbacktrack");
                    packet.cancel=true;

                }

            }
        }
        if(Classes.S12PACKET_ENTITY_VELOCITY.isInstanceof(packet.packet)){
            //System.out.println("onpacket1");
            S12PacketEntityVelocity packetEntityVelocity = new S12PacketEntityVelocity(packet.packet);
            if(packetEntityVelocity.getEntityID()== FunGhostClient.moduleManager.target.target.getEntityID()){
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
