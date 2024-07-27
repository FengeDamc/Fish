package com.fun.client.mods.combat;

import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.client.settings.Setting;
import com.fun.eventapi.event.events.EventTick;
import com.fun.eventapi.event.events.EventUpdate;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

import static com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_LEFT;
import static com.mojang.blaze3d.platform.InputConstants.MOUSE_BUTTON_RIGHT;

public class AutoClicker extends VModule {
    public AutoClicker() {
        super("AutoClicker", Category.Combat);
    }
    public Setting LeftCPS = new Setting("LeftCPS", this, 12, 0, 20, true);
    public Setting RightCPS = new Setting("RightCPS", this, 12, 0, 20, true);
    public Setting left = new Setting("LeftClick", this, true);
    public Setting right = new Setting("RightClick", this, true);

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);
        if(left.getValBoolean()&&mc.mouseHandler.isLeftPressed()){
            if(Math.random() <LeftCPS.getValDouble()/20&&mc.screen==null&&mc.level!=null){
                sendClick(Type.LEFT);
            }
        }
        if(right.getValBoolean()&&mc.mouseHandler.isRightPressed()){
            if(Math.random() <RightCPS.getValDouble()/20&&mc.screen==null&&mc.level!=null){
                sendClick(Type.RIGHT);
            }
        }
    }

    public static enum Type{
        LEFT,RIGHT
    }
    public void sendClick(Type t){
        KeyMapping.click(InputConstants.Type.MOUSE.getOrCreate(t==Type.LEFT?MOUSE_BUTTON_LEFT:MOUSE_BUTTON_RIGHT));
    }
    public InputConstants.Key getKey(Type t){
       return  (InputConstants.Type.MOUSE.getOrCreate(t==Type.LEFT?MOUSE_BUTTON_LEFT:MOUSE_BUTTON_RIGHT));
    }
}
