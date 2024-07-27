package com.fun.client.mods.render;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.eventapi.event.events.EventMotion;
import com.fun.eventapi.event.events.EventView;
import net.minecraft.client.model.PlayerModel;


public class Rotations extends VModule {
    public Rotations() {
        super("Rotations", Category.Render);
    }

    @Override
    public void onView(EventView event) {
        super.onView(event);
        if(mc.player!=null&& FunGhostClient.rotationManager.isActive()) {
            mc.player.setYBodyRot(FunGhostClient.rotationManager.getRation().y);
            mc.player.setYHeadRot(FunGhostClient.rotationManager.getRation().y);
        }
    }

    @Override
    public void onMotion(EventMotion event) {
        super.onMotion(event);
    }
}
