package fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventMoment;
import com.darkmagician6.eventapi.event.events.EventPacket;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.client.settings.Setting;
import fun.inject.inject.wrapper.impl.network.packets.server.S12PacketEntityVelocity;
import fun.utils.Classes;

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

        if(Classes.S12PACKET_ENTITY_VELOCITY.getVClass().isInstanceof(packet.packet)){
            //Agent.logger.info(Mappings.getUnobfClass(packet.packet));
            S12PacketEntityVelocity packetVelocity=null;
            try{
                //Agent.logger.info("s12");
                packetVelocity=new S12PacketEntityVelocity(packet.packet);
                //Agent.logger.info("entityid:{} playerid:{}",packetVelocity.getEntityID(),mc.getPlayer().getEntityID());
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
        //Agent.logger.info(mode.getValString());

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
