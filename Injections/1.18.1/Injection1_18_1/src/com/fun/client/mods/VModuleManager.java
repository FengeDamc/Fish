package com.fun.client.mods;

import com.fun.client.mods.combat.AimBot;
import com.fun.client.mods.combat.AutoClicker;
import com.fun.client.mods.combat.Reach;
import com.fun.client.mods.combat.Target;
import com.fun.client.mods.movement.KeepSprint;
import com.fun.client.mods.render.HUD;
import com.fun.client.mods.render.NotificationModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class VModuleManager {
    public KeepSprint sprint;
    public HUD hud;
    public NotificationModule notification;
    public Reach reach;
    public AutoClicker autoClicker;
    public Target target;
    public AimBot aimBot;
    public void init(){
        Minecraft.getInstance().getWindow().setTitle("钓鱼岛");
        sprint=new KeepSprint();
        hud=new HUD();
        notification=new NotificationModule();
        reach=new Reach();
        autoClicker=new AutoClicker();
        target=new Target();
        aimBot=new AimBot();
    }
}
