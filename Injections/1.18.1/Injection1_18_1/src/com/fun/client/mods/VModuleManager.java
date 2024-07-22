package com.fun.client.mods;

import com.fun.client.mods.combat.AimBot;
import com.fun.client.mods.combat.AutoClicker;
import com.fun.client.mods.combat.Reach;
import com.fun.client.mods.combat.Target;
import com.fun.client.mods.movement.KeepSprint;
import com.fun.client.mods.render.HUD;
import com.fun.client.mods.render.NotificationModule;
import com.fun.client.mods.world.Eagle;
import com.fun.inject.injection.wrapper.impl.setting.GameSettingsWrapper;
import com.fun.utils.version.methods.Methods;
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
    public Eagle eagle;
    public Minecraft mc=Minecraft.getInstance();
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
    }
    public void mouseFix() {
        //mc.getGameSettings().getKey(GameSettingsWrapper.USE).setPressed((boolean) Methods.isButtonDown.invoke(null,1));
        //mc.getGameSettings().getKey(GameSettingsWrapper.ATTACK).setPressed((boolean)Methods.isButtonDown.invoke(null,0));

    }
}
