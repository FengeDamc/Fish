package com.fun.client.mods.render;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.client.mods.render.notify.Notification;
import com.fun.eventapi.event.events.EventStrafe;
import com.fun.eventapi.event.events.EventUpdate;

import javax.vecmath.Vector2f;

public class FreeLook extends VModule {
    public FreeLook() {
        super("FreeLook", Category.Render);
    }
    public Vector2f v;
    @Override
    public void onEnable() {
        super.onEnable();

        if(mc.player!=null)v=new Vector2f(mc.player.getXRot(),mc.player.getYRot());
        else v=new Vector2f();
    }

    @Override
    public void onStrafe(EventStrafe event) {
        super.onStrafe(event);
        event.yaw=v.y;
    }

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);
        FunGhostClient.rotationManager.setRation(v);
    }

}
