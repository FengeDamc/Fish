package com.fun.client.mods;

import com.fun.client.mods.combat.*;
import com.fun.client.mods.movement.KeepSprint;
import com.fun.client.mods.movement.StrafeFix;
import com.fun.client.mods.player.ChestStealer;
import com.fun.client.mods.render.FreeLook;
import com.fun.client.mods.render.HUD;
import com.fun.client.mods.render.NotificationModule;
import com.fun.client.mods.render.Rotations;
import com.fun.client.mods.world.Eagle;
import com.fun.client.mods.world.Scaffold;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;


public class VModuleManager {
    public KeepSprint sprint;
    public HUD hud;
    public NotificationModule notification;
    public Reach reach;
    public AutoClicker autoClicker;
    public Target target;
    public AimBot aimBot;
    public Eagle eagle;
    public StrafeFix strafeFix;
    public Minecraft mc=Minecraft.getInstance();

    public Rotations rotations;
    public FreeLook freeLook;
    public KillAura killAura;
    public Scaffold scaffold;
    public Velocity velocity;
    public ChestStealer chestStealer;
    public void init(){
        Minecraft.getInstance().getWindow().setTitle("钓鱼岛");
        sprint=new KeepSprint();
        hud=new HUD();
        notification=new NotificationModule();
        reach=new Reach();
        autoClicker=new AutoClicker();
        target=new Target();
        aimBot=new AimBot();
        eagle=new Eagle();
        rotations=new Rotations();
        strafeFix=new StrafeFix();
        killAura=new KillAura();
        freeLook =new FreeLook();
        scaffold=new Scaffold();
        velocity=new Velocity();
        chestStealer=new ChestStealer();
    }
    public void mouseFix() {
       //todo
    }
}
