package com.fun.client.mods.combat;

import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.mods.VModule;
import com.fun.eventapi.event.events.EventRender2D;
import com.fun.utils.font.FontManager;
import com.fun.utils.math.PerlinNoise;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.world.WorldSettings;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class KillAura extends VModule {
    public float range = 6.2173613F;
    public float startYaw;
    public float tempYaw;
    public boolean pb;
    public PerlinNoise pn=new PerlinNoise(0.2f);
    public PerlinNoise rpn=new PerlinNoise(0.22f);
    public PerlinNoise rpn2=new PerlinNoise(0.3f);
    public PerlinNoise rpn3=new PerlinNoise(0.1f);
    public PerlinNoise rpn4=new PerlinNoise(0.09f);
    public PerlinNoise rpn5=new PerlinNoise(0.12f);
    private int unblocklag;
    public float aimTickYaw=0;
    public float aimTickPitch =0;
    public float aimMaxTick=1;
    public int disable=0;
    public Vector2f server_rotation = new Vector2f();
    public Vector2f last_server_rotation = new Vector2f();

    private boolean hitting;
    private boolean stopped;
    private int endab;
    public boolean block;
    public int server_slot=0;
    public EntityLivingBase target;
    public KillAura(int key) {
        super("杀戮光环", Category.Combat);
    }

    @Override
    public void onRender2D(EventRender2D e) {

        if (target!=null&&!mc.gameSettings.showDebugInfo){
            ScaledResolution sr = new ScaledResolution(mc);
            Gui.drawRect(sr.getScaledWidth() / 2 + 55,
                    sr.getScaledHeight() / 2 + 55,sr.getScaledWidth() / 2 + 55+100,
                    sr.getScaledHeight() / 2 + 55+50,new Color(65,65,70,125).getRGB());
            Gui.drawRect(sr.getScaledWidth() / 2 + 55,
                    sr.getScaledHeight() / 2 + 55, (int) ((sr.getScaledWidth() / 2 + 55)+100*(target.getHealth()/target.getMaxHealth())),
                    sr.getScaledHeight() / 2 + 55+50,new Color(255,0,0,200).getRGB());

            FontManager.tenacity.drawString("目标名称:"+target.getName(),sr.getScaledWidth()/2+55,sr.getScaledHeight()/2+55,Color.WHITE.getRGB());
            FontManager.tenacity.drawString("目标血量:"+target.getHealth()+"/"+target.getMaxHealth(),sr.getScaledWidth()/2+55,sr.getScaledHeight()/2+55+25,Color.yellow.getRGB());
            FontManager.tenacity.drawString("blocking:"+(target instanceof EntityPlayer &&((EntityPlayer) target).isBlocking()),sr.getScaledWidth()/2+55,sr.getScaledHeight()/2+55+50,Color.yellow.getRGB());


        }
    }





    @Override
    public void onEnable() {
        super.onEnable();
        startYaw = mc.thePlayer.rotationYaw;
        tempYaw = mc.thePlayer.rotationYaw;
    }


    public int getSlot() {
        for (int i = 0; i <= 8; i++) {
            if ((mc.thePlayer.inventory.mainInventory[i])==null){
                continue;
            }
            if ((mc.thePlayer.inventory.mainInventory[i]).getItem() instanceof ItemSword) {
                return i;
            }
        }
        return mc.thePlayer.inventory.currentItem;
    }



}
