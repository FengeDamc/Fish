package com.fun.client.mods.combat;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.VModule;
import com.fun.client.settings.Setting;
import com.fun.eventapi.event.events.EventMoment;
import com.fun.eventapi.event.events.EventPacket;

import java.util.Random;

import static com.fun.client.FunGhostClient.registerManager;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.utils.PacketUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class Velocity extends VModule {
    private final Random random = new Random();

    public Velocity() {
        super("Velocity", Category.Combat);
    }

    //public Entity target = null;

    public Setting mode = new Setting("Mode", this, "Vanilla", new String[]{"Vanilla", "JumpReset","GrimNoXZ"});
    public Setting noxzCount=new Setting("NoxzCount",this,100,20,200,true){
        @Override
        public boolean isVisible() {
            return mode.getValString().equalsIgnoreCase("GrimNoXZ");
        }
    };
    public Setting horizontalMin = new Setting("Horizontal Min", this, 80.0, 0.0, 100.0, false) {
        @Override
        public boolean isVisible() {
            return mode.getValString().equalsIgnoreCase("Vanilla");
        }
    };
    public Setting horizontalMax = new Setting("Horizontal Max", this, 100.0, 0.0, 100.0, false) {
        @Override
        public boolean isVisible() {
            return mode.getValString().equalsIgnoreCase("Vanilla");
        }
    };
    public Setting verticalMin = new Setting("Vertical Min", this, 80.0, 0.0, 100.0, false) {
        @Override
        public boolean isVisible() {
            return mode.getValString().equalsIgnoreCase("Vanilla");
        }
    };
    public Setting verticalMax = new Setting("Vertical Max", this, 100.0, 0.0, 100.0, false) {
        @Override
        public boolean isVisible() {
            return mode.getValString().equalsIgnoreCase("Vanilla");
        }
    };
    public Setting chance = new Setting("Chance", this, 100.0, 0.0, 100.0, true);
    public Setting waterCheck = new Setting("WaterCheck", this, true);

    private double getRandomMultiplier(double min, double max) {
        return min + (max - min) * Math.random();
    }

    @Override
    public void onPacket(EventPacket packet) {
        super.onPacket(packet);

        if (packet.packet instanceof ClientboundSetEntityMotionPacket) {
            try {
                if (((ClientboundSetEntityMotionPacket) packet.packet).getId() == mc.player.getId()) {
                    // 更新 target
                    /*target = registerManager.vModuleManager.target.target;
                    if (target == null) {
                        return; // 如果没有目标，退出
                    }*/

                    if (this.mode.getValString().equalsIgnoreCase("Vanilla")) {
                        System.out.println(0);
                        if (waterCheck.getValBoolean() && mc.player.isInWater()) {
                            return;
                        }
                        System.out.println("1");
                        if (random.nextDouble() * 100 <= chance.getValDouble()) {

                            double horizontalMultiplier = getRandomMultiplier(horizontalMin.getValDouble(), horizontalMax.getValDouble()) / 100.0;
                            double verticalMultiplier = getRandomMultiplier(verticalMin.getValDouble(), verticalMax.getValDouble()) / 100.0;
                            Vec3 newVec= new Vec3(((ClientboundSetEntityMotionPacket) packet.packet).getXa()/8000.0f* horizontalMultiplier,
                                    ((ClientboundSetEntityMotionPacket) packet.packet).getYa()/8000.0f* verticalMultiplier,
                                    ((ClientboundSetEntityMotionPacket) packet.packet).getZa()/8000.0f* horizontalMultiplier);
                            System.out.println("1.5");
                            ClientboundSetEntityMotionPacket newPacket= new ClientboundSetEntityMotionPacket(((ClientboundSetEntityMotionPacket) packet.packet).getId(),newVec);
                            System.out.println("2");
                            PacketUtils.receivePacketNoEvent(newPacket);
                            System.out.println("3");
                            packet.cancel=true;

                        }
                    }
                    if(mode.getValString().equalsIgnoreCase("GrimNoXZ")) {
                        PacketUtils.receivePacketNoEvent((Packet) packet.packet);
                        if(registerManager.vModuleManager.target.target!=null){
                            for(int i=0;i<noxzCount.getValDouble();i++){
                                PacketUtils.sendPacket(ServerboundInteractPacket.createAttackPacket(registerManager.vModuleManager.target.target, mc.player.isShiftKeyDown()));
                                mc.player.setSprinting(false);
                                mc.player.setDeltaMovement(mc.player.getDeltaMovement().multiply(0.6,1.0,0.6));
                                PacketUtils.sendPacket(new ServerboundSwingPacket(InteractionHand.MAIN_HAND));
                            }
                        }
                        packet.cancel=true;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMoment(EventMoment event) {
        super.onMoment(event);
        // 更新 target
        /*target = registerManager.vModuleManager.target.target;
        if (target == null) {
            return; // 如果没有目标，退出
        }*/

        if (mode.getValString().equalsIgnoreCase("JumpReset")) {
            if (mc.player.getHealth() == 9 && mc.player.isOnGround()) {
                if (waterCheck.getValBoolean() && mc.player.isInWater()) {
                    return;
                }
                if (random.nextDouble() * 100 < chance.getValDouble()) {
                    mc.player.input.jumping=true;
                }
            }
        }
    }

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);
        //

    }
}