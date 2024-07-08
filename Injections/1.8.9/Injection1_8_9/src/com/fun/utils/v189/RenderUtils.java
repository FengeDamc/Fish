package com.fun.utils.v189;

import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;
import com.fun.inject.injection.wrapper.impl.other.TimerWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Timer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glDepthMask;

public class RenderUtils {
    public static Minecraft mc=Minecraft.getMinecraft();
    public static void glColor(final int red, final int green, final int blue, final int alpha) {
        GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

}
