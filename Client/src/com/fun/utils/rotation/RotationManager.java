package com.fun.utils.rotation;


import com.fun.eventapi.EventTarget;
import com.fun.eventapi.event.events.EventMotion;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;


import javax.vecmath.Vector2f;

public class RotationManager {
    public Vector2f ration=new Vector2f();
    public MinecraftWrapper mc = MinecraftWrapper.get();
    private boolean active;
    private int tick=0;

    public void setRation(Vector2f ration) {
        if(ration.equals(getLocalPlayer()))return;
        this.ration = ration;
        active=true;
    }
    public Vector2f getRation() {
        return isActive()?ration:new Vector2f(mc.getPlayer().getPitch(),mc.getPlayer().getYaw());
    }
    public Vector2f getLocalPlayer() {
        return new Vector2f(mc.getPlayer().getPitch(),mc.getPlayer().getYaw());
    }
    @EventTarget
    public void onMotion(EventMotion event) {
        event.yaw=getRation().y;
        event.pitch=getRation().x;
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        setActive(active);
        active=false;
    }

    public boolean isActive() {
        return tick>0;
    }
    public void setActive(boolean active) {
        tick=active?1:0;
    }
}
