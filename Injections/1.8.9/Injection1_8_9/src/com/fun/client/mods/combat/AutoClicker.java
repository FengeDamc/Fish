package com.fun.client.mods.combat;

import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.eventapi.event.events.EventTick;
import com.fun.inject.injection.wrapper.impl.setting.KeyBindingWrapper;
import com.fun.utils.version.fields.Fields;
import com.fun.utils.version.methods.Methods;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoClicker extends Module {

    public Setting LeftCPS = new Setting("LeftCPS", this, 12, 0, 20, true);
    public Setting RightCPS = new Setting("RightCPS", this, 12, 0, 20, true);
    public Setting left = new Setting("LeftClick", this, true);
    public Setting right = new Setting("RightClick", this, true);

    private static final Logger LOGGER = Logger.getLogger(AutoClicker.class.getName());

    public AutoClicker() {
        super("AutoClicker", Category.Combat);
        try {
            new Robot();
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize Robot", e);
        }
    }

    @Override
    public void onTick(EventTick event) {
        super.onTick(event);
        handleClickLogic(); // 处理点击逻辑
    }

    private void handleClickLogic() {
        // 左键点击
        if (Math.random() < LeftCPS.getValDouble() / 20) {
            if (mc.getGameSettings().getKey("key.attack").isPressed() && left.getValBoolean()) {
                sendClick(mc.getGameSettings().getKey("key.attack"), true);
            }
        }

        // 右键点击
        if (Math.random() < RightCPS.getValDouble() / 20) {
            if (mc.getGameSettings().getKey("key.use").isPressed() && right.getValBoolean()) {
                if (!mc.getPlayer().isUsingItem()) {
                    sendClick(mc.getGameSettings().getKey("key.use"), true);
                }
            }
        }
    }

    public void sendClick(final KeyBindingWrapper button, final boolean state) {
        final int keyBind = button.getKeyCode();
        Fields.pressTime_KeyBinding.set(button.keyBindingObj, 0);
        Methods.setKeyBindState_KeyBinding.invoke(null, keyBind, state);
        if (state) {
            Methods.onTick_KeyBinding.invoke(null, keyBind);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
