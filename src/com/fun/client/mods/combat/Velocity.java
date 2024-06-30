package com.fun.client.mods.combat;

import com.fun.eventapi.event.events.EventMoment;
import com.fun.eventapi.event.events.EventPacket;
import com.fun.inject.Agent;
import com.fun.inject.inject.Mappings;
import com.fun.utils.Classes;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.inject.inject.wrapper.impl.network.packets.server.S12PacketEntityVelocity;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Category.Combat);
    }
    public Setting mode = new Setting("Mode",this,"Vanilla",new String[]{"Vanilla","JumpReset"});
    public Setting x =new Setting("X-Axis",this,0,0,1,false)
    {
        @Override
        public boolean isActive() {
            return mode.sval.equalsIgnoreCase("vanilla");
        }
    };
    public Setting y =new Setting("Y-Axis",this,0,0,1,false){
        @Override
        public boolean isActive() {
            return mode.sval.equalsIgnoreCase("vanilla");
        }
    };
    public Setting z =new Setting("Z-Axis",this,0,0,1,false){
        @Override
        public boolean isActive() {
            return mode.sval.equalsIgnoreCase("vanilla");
        }
    };


    @Override
    public void onPacket(EventPacket packet) {
        super.onPacket(packet);
        if(Classes.S12PACKET_ENTITY_VELOCITY.isInstanceof(packet.packet)){
            //Agent.System.out.println(Mappings.getUnobfClass(packet.packet));
            S12PacketEntityVelocity packetVelocity=null;
            try{
                //Agent.System.out.println("s12");
                packetVelocity=new S12PacketEntityVelocity(packet.packet);
                //Agent.System.out.println("entityid:{} playerid:{}",packetVelocity.getEntityID(),mc.getPlayer().getEntityID());
                if(packetVelocity.getEntityID()==mc.getPlayer().getEntityID()) {
                    if (this.mode.sval.equalsIgnoreCase("Vanilla")) {
                        packetVelocity.setMotionX((int) (packetVelocity.getMotionX()*x.getValDouble()));
                        packetVelocity.setMotionY((int) (packetVelocity.getMotionY()*y.getValDouble()));
                        packetVelocity.setMotionZ((int) (packetVelocity.getMotionZ()*z.getValDouble()));
                    }
                }
            }
            catch (NullPointerException e){
            }




        }
        //Agent.System.out.println(mode.getValString());

    }



    @Override
    public void onMoment(EventMoment event) {
        super.onMoment(event);
        if(mode.sval.equalsIgnoreCase("jumpreset")){
            if(mc.getPlayer().getHurtTime()==9&&mc.getPlayer().isOnGround()){
                mc.getPlayer().getMovementInputObj().setJump(true);
            }

        }

    }
}
