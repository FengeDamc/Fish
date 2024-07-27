package com.fun.client.mods;


import com.fun.eventapi.EventManager;
import com.fun.eventapi.EventTarget;
import com.fun.eventapi.event.events.*;
import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.inject.injection.wrapper.impl.setting.GameSettingsWrapper;
import com.fun.utils.version.methods.Methods;

import java.util.ArrayList;

import static com.fun.client.utils.Rotation.Rotation.mc;

public class RegisterManager {
    public ArrayList<Module> mods = new ArrayList<>();
    public VModuleManager vModuleManager;




    public void init() {
        mods.clear();
        EventManager.register(this);
        EventManager.register(FunGhostClient.rotationManager);
        try {
            vModuleManager = new VModuleManager();
            vModuleManager.init();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        //System.out.println("update");
        for (Module m : FunGhostClient.registerManager.mods) {
            //System.out.println(m.getName());

            if (m.running) {
                m.onUpdate(event);
            }
        }
        vModuleManager.notification.tick();


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
        }//EventView
    }
    @EventTarget
    public void onView(EventView event) {
        for (Module m : mods) {
            if (m.running) m.onView(event);
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
        vModuleManager.notification.render(event);
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
        vModuleManager.mouseFix();
        for (Module m : mods) {
            if (m.running){

                m.onTick(event);
            }
        }
    }
    @EventTarget
    public void onJump(EventJump event){
        vModuleManager.mouseFix();
        for (Module m : mods) {
            if (m.running){

                m.onJump(event);
            }
        }



    }

}
