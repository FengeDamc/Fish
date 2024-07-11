package com.fun.client.mods.combat;

import com.fun.eventapi.event.events.EventMoment;
import com.fun.eventapi.event.events.EventPacket;
import com.fun.utils.version.clazz.Classes;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.inject.injection.wrapper.impl.network.packets.server.S12PacketEntityVelocityWrapper;
import com.fun.inject.injection.wrapper.impl.entity.EntityWrapper;
import javax.vecmath.Vector2f;

import static com.fun.client.FunGhostClient.moduleManager;

import java.util.Random;

public class Velocity extends Module {
    private final Random random = new Random();

    public Velocity() {
        super("Velocity", Category.Combat);
    }

    public EntityWrapper target = null;

    public Setting mode = new Setting("Mode", this, "Vanilla", new String[]{"Vanilla", "JumpReset"});
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
        return min + (max - min) * random.nextDouble();
    }

    @Override
    public void onPacket(EventPacket packet) {
        super.onPacket(packet);
        if (Classes.S12PACKET_ENTITY_VELOCITY.isInstanceof(packet.packet)) {
            try {
                S12PacketEntityVelocityWrapper packetVelocity = new S12PacketEntityVelocityWrapper(packet.packet);
                if (packetVelocity.getEntityID() == mc.getPlayer().getEntityID()) {
                    // 更新 target
                    target = moduleManager.target.target;
                    if (target == null) {
                        return; // 如果没有目标，退出
                    }

                    if (this.mode.getValString().equalsIgnoreCase("Vanilla")) {
                        if (waterCheck.getValBoolean() && mc.getPlayer().isInWater()) {
                            return;
                        }
                        if (random.nextDouble() * 100 < chance.getValDouble()) {
                            double horizontalMultiplier = getRandomMultiplier(horizontalMin.getValDouble(), horizontalMax.getValDouble()) / 100.0;
                            double verticalMultiplier = getRandomMultiplier(verticalMin.getValDouble(), verticalMax.getValDouble()) / 100.0;
                            packetVelocity.setMotionX((int) (packetVelocity.getMotionX() * horizontalMultiplier));
                            packetVelocity.setMotionY((int) (packetVelocity.getMotionY() * verticalMultiplier));
                            packetVelocity.setMotionZ((int) (packetVelocity.getMotionZ() * horizontalMultiplier));
                        }
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
        target = moduleManager.target.target;
        if (target == null) {
            return; // 如果没有目标，退出
        }

        if (mode.getValString().equalsIgnoreCase("JumpReset")) {
            if (mc.getPlayer().getHurtTime() == 9 && mc.getPlayer().isOnGround()) {
                if (waterCheck.getValBoolean() && mc.getPlayer().isInWater()) {
                    return;
                }
                if (random.nextDouble() * 100 < chance.getValDouble()) {
                    mc.getPlayer().getMovementInputObj().setJump(true);
                }
            }
        }
    }
}