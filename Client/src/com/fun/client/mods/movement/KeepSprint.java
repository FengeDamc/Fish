package com.fun.client.mods.movement;

import com.fun.eventapi.event.events.EventUpdate;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import org.lwjgl.input.Keyboard;

public class KeepSprint extends Module {
    public KeepSprint(String nameIn) {
        super(Keyboard.KEY_R,nameIn, Category.Movement);
    }
    public Setting setting=new Setting("Test",this,false){
        @Override
        public boolean isVisible() {
            return !running;
        }
    };

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);
        //Agent.System.out.println("update");
        mc.getPlayer().setSprinting(true);
    }
}
