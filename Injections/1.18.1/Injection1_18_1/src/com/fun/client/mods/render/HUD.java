package com.fun.client.mods.render;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.client.utils.ColorUtils;
import com.fun.eventapi.event.events.EventRender2D;
import com.fun.inject.injection.wrapper.impl.gui.ScaledResolutionWrapper;
import com.fun.utils.RenderManager;
import com.fun.client.font.FontManager;
import com.fun.utils.render.RenderUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.GlowSquidRenderer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.client.EffectRenderer;

import javax.vecmath.Vector2d;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;



public class HUD extends Module {
    public HUD() {
        super(InputConstants.KEY_R,"HUD", Category.Render);
        //this.setRunning(true);

    }
    public Setting mixColor1=new Setting("MixColor1",this,0xffffff,0x000000,0xffffff,true);
    public Setting mixColor2=new Setting("MixColor2",this,0xffffff,0x000000,0xffffff,true);


    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        FontManager.tenacity.drawStringWithShadow(RenderManager.currentPoseStack,"FISH", 4, 4, new Color(78, 255, 166, 235).getRGB());
        renderArrayList();



    }
    private HashSet<String> modBlacklist = new HashSet<>();

    private void renderArrayList() {
        int yCount = 0;
        int index = 0;
        long x = 0;
        ArrayList<Module> mods = FunGhostClient.registerManager.mods;
        ArrayList<Module> running=new ArrayList<Module>();

        for (Module m : mods) {
            if (m.running)
                running.add(m);

        }
        for(Module ignored :running)
            for (int i = 0, runningSize = running.size(); i < runningSize; i++) {
                Module m = running.get(i);
                if (i<runningSize-1&& FontManager.tenacity.getStringWidth((m.getName()))<FontManager.tenacity.getStringWidth(running.get(i+1).getName())){
                    running.set(i,running.get(i+1));
                    running.set(i+1,m);

                }
            }

        for(Module m:running){
            ScaledResolutionWrapper sr=new ScaledResolutionWrapper(mc);
            double offset = yCount * (FontManager.tenacity.getFontHeight("F") + 6);//Minecraft.getInstance().font.width(

            if (m.running) {
                if (!modBlacklist.contains(m.getClass().getName())) {
                    int color=ColorUtils.mixColors(new Color((int) mixColor1.getValDouble()),new Color((int) mixColor2.getValDouble()),ColorUtils.getBlendFactor(new Vector2d(sr.getScaledWidth() -FontManager.tenacity.getStringWidth(m.getName())-6, (int) offset))).getRGB();
                    RenderManager.drawRoundedRect(sr.getScaledWidth(), (int) (2 + offset),sr.getScaledWidth()+2,(int) (-2 + offset+FontManager.tenacity.getFontHeight("F")),3,new Color(color).getRGB());
                    RenderManager.drawRoundedRect((int) (sr.getScaledWidth() - FontManager.tenacity.getStringWidth(m.getName())-6), (int) offset+2, sr.getScaledWidth(), (int) (FontManager.tenacity.getFontHeight("F") + offset),5,new Color(225, 242, 255, 105).getRGB());
                    FontManager.tenacity.drawStringWithShadow(RenderManager.currentPoseStack,m.getName(),
                            sr.getScaledWidth() -FontManager.tenacity.getStringWidth(m.getName())-4, (int) (offset+4),color);//FontManager.tenacity.drawStringWithShadow( m.getName(), sr.getScaledWidth() -FontManager.tenacity.getStringWidth(m.getName())-4, (int) (4 + offset),color);
                    yCount++;
                    index++;
                    x++;
                }
            }
        }
        //System.out.println("hud");
    }
}
