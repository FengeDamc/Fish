package com.fun.client.mods;

import com.fun.client.mods.world.Backtrack;
import com.fun.eventapi.EventManager;
import com.fun.eventapi.EventTarget;
import com.fun.eventapi.event.events.*;
import com.fun.client.mods.combat.*;
import com.fun.client.mods.movement.Flight;
import com.fun.client.mods.movement.KeepSprint;
import com.fun.client.mods.render.HUD;
import com.fun.client.mods.render.NotificationModule;
import com.fun.client.mods.world.Eagle;
import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.inject.injection.wrapper.impl.setting.GameSettingsWrapper;
import com.fun.utils.version.methods.Methods;

import java.util.ArrayList;

import static com.fun.client.utils.Rotation.Rotation.mc;

public class ModuleManager {
    public ArrayList<Module> mods = new ArrayList<>();
    public VModuleManager vModuleManager;

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
    public Backtrack backtrack;

    public void init() {
        mods.clear();
        EventManager.register(this);
        vModuleManager=new VModuleManager();
        keepSprint = new KeepSprint("KeepSprint");
        hud = new HUD("HUD");
        reach = new Reach();
        autoClicker=new AutoClicker();
        aimAssist = new AimAssist();
        flight = new Flight("Flight",Category.Movement);
        eagle=new Eagle();
        velocity=new Velocity();
        autoBlocking=new AutoBlocking();
        notification=new NotificationModule();
        target=new Target();
        vModuleManager.init();

    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        //System.out.println("update");
        for (Module m : FunGhostClient.moduleManager.mods) {
            //System.out.println(m.getName());

            if (m.running) {
                m.onUpdate(event);
            }
        }


    }

    public Module getModule(String name) {
        for (Module m : mods) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    @EventTarget
    public synchronized void onPacket(EventPacket event) {
        //Agent.System.out.println(Mappings.getUnobfClass(event.packet.getClass().getName()));
        for (Module m : mods) {
            if (m.running) m.onPacket(event);

        }

    }
    @EventTarget
    public void onStrafe(EventStrafe event) {
        //Agent.System.out.println("onStrafe yaw:{} forward:{} strafe:{}",event.yaw,event.forward,event.strafe);
        //System.out.println("onStrafe2");
        for (Module m : mods) {
            if (m.running) m.onStrafe(event);
        }
    }
    @EventTarget
    public void onMotion(EventMotion event) {
        //Agent.System.out.println("onStrafe yaw:{} forward:{} strafe:{}",event.yaw,event.forward,event.strafe);
        for (Module m : mods) {
            if (m.running) m.onMotion(event);
        }
    }


    @EventTarget
    public void onKey(EventKey event) {
        for (Module m : mods) {
            if (event.key != 0 && event.key == m.key) {
                m.setRunning(!m.running);
            }
            if (m.running) m.onKey(event);
        }
        if (event.key == 1) ConfigModule.saveConfig();
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {

        for (Module m : mods) {
            if (m.running) m.onRender3D(event);
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        //System.out.println("render2d");
        for (Module m : mods) {
            if (m.running) m.onRender2D(event);
        }
        notification.render(event);
    }

    @EventTarget
    public void onAttackReach(EventAttackReach event) {
        for (Module m : mods) {
            if (m.running) m.onAttackReach(event);
        }
    }

    @EventTarget
    public void onBlockReach(EventBlockReach event) {
        for (Module m : mods) {
            if (m.running) m.onBlockReach(event);
        }
    }
    @EventTarget
    public void onMoment(EventMoment event){
        for (Module m : mods) {
            if (m.running) m.onMoment(event);
        }
    }
    @EventTarget
    public void onTick(EventTick event){
        mc.getGameSettings().getKey(GameSettingsWrapper.USE).setPressed((boolean)Methods.isButtonDown.invoke(null,1));
        mc.getGameSettings().getKey(GameSettingsWrapper.ATTACK).setPressed((boolean)Methods.isButtonDown.invoke(null,0));
        for (Module m : mods) {
            if (m.running){

                m.onTick(event);
            }
        }
        notification.tick(event);

    }

}
