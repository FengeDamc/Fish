package net.fun.client.mods.movement;

import com.darkmagician6.eventapi.event.events.EventUpdate;
import net.fun.client.mods.Category;
import net.fun.client.mods.Module;

public class Flight extends Module {
    public Flight(String nameIn, Category category) {
        super(nameIn, category);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);
        mc.getPlayer().setMotionY(mc.getGameSettings().getKey("key.sneak").isPressed()?-0.1:mc.getGameSettings().getKey("key.jump").isPressed()?0.1:0);
    }
}
