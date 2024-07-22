package com.fun.client.mods;

import com.fun.client.mods.combat.*;
import com.fun.client.mods.movement.Flight;
import com.fun.client.mods.movement.KeepSprint;
import com.fun.client.mods.render.HUD;
import com.fun.client.mods.render.NotificationModule;
import com.fun.client.mods.world.Backtrack;
import com.fun.client.mods.world.Eagle;
import com.fun.inject.injection.wrapper.impl.setting.GameSettingsWrapper;
import com.fun.utils.version.methods.Methods;

import static com.fun.client.utils.Rotation.Rotation.mc;

public class VModuleManager {
    public Backtrack backtrack;
    public KeepSprint keepSprint;
    public HUD hud;
    public Reach reach;
    public AimAssist aimAssist;
    public Flight flight;
    public AutoClicker autoClicker;
    public Eagle eagle;
    public Velocity velocity;
    public AutoBlocking autoBlocking;
    public NotificationModule notification;
    public Target target;
    public void init(){
        try{
            backtrack = new Backtrack();
            keepSprint = new KeepSprint("KeepSprint");
            hud = new HUD("HUD");
            reach = new Reach();
            autoClicker = new AutoClicker();
            aimAssist = new AimAssist();
            flight = new Flight("Flight", Category.Movement);
            eagle = new Eagle();
            velocity = new Velocity();
            autoBlocking = new AutoBlocking();
            notification = new NotificationModule();
            target = new Target();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void mouseFix() {
        mc.getGameSettings().getKey(GameSettingsWrapper.USE).setPressed((boolean) Methods.isButtonDown.invoke(null,1));
        mc.getGameSettings().getKey(GameSettingsWrapper.ATTACK).setPressed((boolean)Methods.isButtonDown.invoke(null,0));

    }
}
