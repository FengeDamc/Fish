package com.fun.client.mods.combat;

import com.fun.eventapi.event.events.EventTick;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.inject.inject.wrapper.impl.setting.KeyBindingWrapper;
import com.fun.utils.version.fields.Fields;
import com.fun.utils.version.methods.Methods;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoClicker extends Module {

    public Setting LeftCPS = new Setting("LeftCPS", this, 12, 0, 20, true);
    public Setting RightCPS = new Setting("RightCPS", this, 12, 0, 20, true);
    public Setting left = new Setting("LeftClick", this, true);
    public Setting right = new Setting("RightClick", this, true);
    public Setting doubleMode = new Setting("Double Mode", this, false);

    private Robot robot;
    private volatile boolean isLeftClicking = false;
    private volatile boolean isRightClicking = false;
    private Timer leftClickTimer;
    private Timer rightClickTimer;
    private static final Logger LOGGER = Logger.getLogger(AutoClicker.class.getName());

    public AutoClicker() {
        super("AutoClicker", Category.Combat);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize Robot", e);
        }
    }

    @Override
    public void onTick(EventTick event) {
        super.onTick(event);

        if (doubleMode.getValBoolean()) {
            // New logic for double mode
            handleNewLogic();
        } else {
            // Old logic
            handleOldLogic();
        }
    }

    private void handleNewLogic() {
        if (mc.getGameSettings().getKey("key.attack").isPressed() && left.getValBoolean()) {
            if (!isLeftClicking) {
                isLeftClicking = true;
                leftClickTimer = startClickingTimer(LeftCPS.getValDouble(), InputEvent.BUTTON1_DOWN_MASK);
            }
        } else {
            isLeftClicking = false;
            if (leftClickTimer != null) {
                leftClickTimer.cancel();
            }
        }

        if (mc.getGameSettings().getKey("key.use").isPressed() && right.getValBoolean()) {
            if (!isRightClicking) {
                isRightClicking = true;
                rightClickTimer = startClickingTimer(RightCPS.getValDouble(), InputEvent.BUTTON3_DOWN_MASK);
            }
        } else {
            isRightClicking = false;
            if (rightClickTimer != null) {
                rightClickTimer.cancel();
            }
        }
    }

    private void handleOldLogic() {
        if (Math.random() < LeftCPS.getValDouble() / 20) {
            if (mc.getGameSettings().getKey("key.attack").isPressed() && left.getValBoolean()) {
                sendClick(mc.getGameSettings().getKey("key.attack"), true);
            }
        }

        if (Math.random() < RightCPS.getValDouble() / 20) {
            if (mc.getGameSettings().getKey("key.use").isPressed() && right.getValBoolean()) {
                if (!mc.getPlayer().isUsingItem()) {
                    sendClick(mc.getGameSettings().getKey("key.use"), true);
                }
            }
        }
    }

    private Timer startClickingTimer(double cps, int mouseButton) {
        Timer clickTimer = new Timer(true);
        clickTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendClick(mouseButton);
            }
        }, 0, (long) (1000 / cps));
        return clickTimer;
    }

    public void sendClick(int mouseButton) {
        robot.mousePress(mouseButton);
        robot.mouseRelease(mouseButton);
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
        isLeftClicking = false;
        isRightClicking = false;
        if (leftClickTimer != null) {
            leftClickTimer.cancel();
        }
        if (rightClickTimer != null) {
            rightClickTimer.cancel();
        }
    }
}
