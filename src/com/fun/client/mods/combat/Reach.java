package com.fun.client.mods.combat;

import com.fun.eventapi.event.events.EventAttackReach;
import com.fun.eventapi.event.events.EventBlockReach;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.inject.inject.wrapper.impl.MinecraftWrapper;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {
    public Setting attackRange = new Setting("AttackRange", this, 6.0, 3.0, 6.0, false);
    public Setting blockRange = new Setting("BlockRange", this, 6.0, 4.5, 6.0, false);
    public Setting sprintCheck = new Setting("SprintCheck", this, true);

    public Reach() {
        super(Keyboard.KEY_G, "Reach", Category.Combat);
    }

    @Override
    public void onAttackReach(EventAttackReach event) {
        super.onAttackReach(event);
        if (mc.getPlayer() != null) {
            if (!sprintCheck.getValBoolean() || mc.getPlayer().isSprinting()) {
                event.reach = attackRange.getValDouble();
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
}