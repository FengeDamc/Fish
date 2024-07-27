package com.fun.client.mods.render;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.eventapi.event.events.EventView;


public class Rotations extends VModule {
    public Rotations() {
        super("Rotations", Category.Render);
    }

    @Override
    public void onView(EventView event) {
        super.onView(event);
        if(mc.player!=null&& FunGhostClient.rotationManager.isActive()) {
            mc.player.rotationYawHead = FunGhostClient.rotationManager.getRation().y;
            mc.player.renderYawOffset= FunGhostClient.rotationManager.getRation().y;
        }
    }
}
