package com.fun.client.mods.render;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.eventapi.event.events.EventView;
import com.fun.utils.rotation.RotationManager;


public class Rotations extends VModule {
    public Rotations() {
        super("Rotations", Category.Render);
    }

    @Override
    public void onView(EventView event) {
        super.onView(event);
        if(mc.thePlayer!=null&& FunGhostClient.rotationManager.isActive()) {
            mc.thePlayer.rotationYawHead = FunGhostClient.rotationManager.getRation().y;
            mc.thePlayer.renderYawOffset= FunGhostClient.rotationManager.getRation().y;
        }
    }
}
