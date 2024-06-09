package fun.client.mods;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.event.events.*;
import fun.client.FunGhostClient;
import fun.client.config.ConfigModule;
import fun.client.mods.combat.*;
import fun.client.mods.movement.Flight;
import fun.client.mods.movement.KeepSprint;
import fun.client.mods.render.HUD;
import fun.client.mods.render.NotificationModule;
import fun.client.mods.world.Eagle;
import fun.inject.inject.wrapper.impl.setting.GameSettingsWrapper;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

import static fun.client.utils.Rotation.Rotation.mc;

public class ModuleManager {
    public ArrayList<Module> mods = new ArrayList<>();

    public KeepSprint keepSprint;


    public HUD hud;
    public Reach reach;
    public AimBot aimBot;
    public Flight flight;
    public AutoClicker autoClicker;
    public Eagle eagle;
    public Velocity velocity;
    public AutoBlocking autoBlocking;
    public NotificationModule notification;
    public Target target;

    public void init() {
        EventManager.register(this);
        keepSprint = new KeepSprint("KeepSprint");
        hud = new HUD("HUD");
        reach = new Reach();
        autoClicker=new AutoClicker();
        aimBot = new AimBot();
        flight = new Flight("Flight",Category.Movement);
        eagle=new Eagle();
        velocity=new Velocity();
        autoBlocking=new AutoBlocking();
        notification=new NotificationModule();
        target=new Target();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {

        for (Module m : FunGhostClient.moduleManager.mods) {
            //Agent.logger.info(m.getName());

            if (m.running) m.onUpdate(event);
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
        //Agent.logger.info(Mappings.getUnobfClass(event.packet.getClass().getName()));
        for (Module m : mods) {
            if (m.running) m.onPacket(event);

        }

    }
    @EventTarget
    public void onStrafe(EventStrafe event) {
        //Agent.logger.info("onStrafe yaw:{} forward:{} strafe:{}",event.yaw,event.forward,event.strafe);
        for (Module m : mods) {
            if (m.running) m.onStrafe(event);
        }
    }
    @EventTarget
    public void onMotion(EventMotion event) {
        //Agent.logger.info("onStrafe yaw:{} forward:{} strafe:{}",event.yaw,event.forward,event.strafe);
        for (Module m : mods) {
            if (m.running) m.onMotion(event);
        }
    }


    @EventTarget
    public void onKey(EventKey event) {
        for (Module m : mods) {
            if (event.key != 0 && event.key == m.key) m.setRunning(!m.running);
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

        for (Module m : mods) {
            if (m.running) m.onRender2D(event);
        }
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
        mc.getGameSettings().getKey(GameSettingsWrapper.USE).setPressed(Mouse.isButtonDown(1));
        mc.getGameSettings().getKey(GameSettingsWrapper.ATTACK).setPressed(Mouse.isButtonDown(0));
        for (Module m : mods) {
            if (m.running) m.onTick(event);
        }
    }

}
