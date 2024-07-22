package com.fun.client.mods.combat;

import com.fun.client.settings.Setting;
import com.fun.eventapi.event.events.EventAttackReach;
import com.fun.eventapi.event.events.EventBlockReach;
import org.lwjgl.input.Keyboard;

import java.util.Random;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;

public class Reach extends Module {
    private final Random random = new Random();
    private long lastRandomUpdateTime = 0;
    private double currentRange;

    public Setting rangeMin = new Setting("RangeMin", this, 3.0, 3.0, 6.0, false);
    public Setting rangeMax = new Setting("RangeMax", this, 6.0, 3.0, 6.0, false);
    public Setting blockRange = new Setting("BlockRange", this, 6.0, 4.5, 6.0, false);
    public Setting sprintCheck = new Setting("SprintCheck", this, true);
    public Setting chance = new Setting("Chance", this, 100.0, 0.0, 100.0, true);
    public Setting verticalCheck = new Setting("VerticalCheck", this, true);
    public Setting verticalAngle = new Setting("VerticalAngle", this, 45.0, 0.0, 90.0, true);
    public Setting waterCheck = new Setting("WaterCheck", this, true);

    public Reach() {
        super(Keyboard.KEY_G, "Reach", Category.Combat);
        updateRandomRange();
    }

    @Override
    public void onAttackReach(EventAttackReach event) {
        super.onAttackReach(event);
        if (mc.getPlayer() != null) {
            if (!sprintCheck.getValBoolean() || mc.getPlayer().isSprinting()) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastRandomUpdateTime >= 1000 + random.nextInt(1001)) {
                    updateRandomRange();
                    lastRandomUpdateTime = currentTime;
                }
                if (random.nextDouble() * 100 < chance.getValDouble() && shouldApplyReach()) {
                    event.reach = currentRange;
                }
            }
        }
    }

    @Override
    public void onBlockReach(EventBlockReach event) {
        super.onBlockReach(event);
        if (mc.getPlayer() != null) {
            if (!sprintCheck.getValBoolean() || mc.getPlayer().isSprinting()) {
                event.reach = blockRange.getValDouble();
            }
        }
    }

    private void updateRandomRange() {
        double min = rangeMin.getValDouble();
        double max = rangeMax.getValDouble();
        currentRange = min + (max - min) * random.nextDouble();
    }

    private boolean shouldApplyReach() {
        if (waterCheck.getValBoolean() && mc.getPlayer().isInWater()) {
            return false;
        }

        if (verticalCheck.getValBoolean()) {
            float pitch = mc.getPlayer().getPitch(); // 获取玩家视角的俯仰角
            float maxAngle = (float) verticalAngle.getValDouble();
            return !(pitch > maxAngle) && !(pitch < -maxAngle);
        }

        return true;
    }
}