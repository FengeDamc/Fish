package com.fun.utils;

import com.fun.inject.injection.wrapper.impl.render.DefaultVertexFormats;
import com.fun.inject.injection.wrapper.impl.render.GlStateManagerWrapper;
import com.fun.inject.injection.wrapper.impl.render.Tessellator;
import com.fun.inject.injection.wrapper.impl.render.WorldRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

public class RenderManager {
    public static PoseStack currentPoseStack;
    public static void drawRoundedRect(int left, int top, int right, int bottom, int radius, int color) {
        left += radius;
        top += radius;
        bottom -= radius;
        right -= radius;
        if (left < right) {
            int i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            int j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (color >> 24 & 0xFF) / 255.0F;
        float f = (color >> 16 & 0xFF) / 255.0F;
        float f1 = (color >> 8 & 0xFF) / 255.0F;
        float f2 = (color & 0xFF) / 255.0F;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuilder();
        GlStateManagerWrapper.enableBlend();//enableBlend ()V func_179147_l
        GlStateManagerWrapper.disableTexture2D();//disableTexture2D ()V func_179090_x
        GlStateManagerWrapper.tryBlendFuncSeparate(770, 771, 1, 0);//tryBlendFuncSeparate (IIII)V func_179120_a
        GlStateManagerWrapper.color(f, f1, f2, f3);
        worldrenderer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);//POSITION field_181705_e
        //begin (ILnet/minecraft/client/renderer/vertex/VertexFormat;)V func_181668_a
        for (int cornerId = 0; cornerId < 4; cornerId++) {
            int ky = (cornerId + 1) / 2 % 2;
            int kx = cornerId / 2;
            double x = (kx != 0) ? right : left;
            double y = (ky != 0) ? bottom : top;
            for (int a = 0; a <= 8; a++)
                worldrenderer.vertex(x + Math.sin(0.19634954084936207D * a + Math.PI * cornerId / 2.0D) * radius, y + Math.cos(0.19634954084936207D * a + Math.PI * cornerId / 2.0D) * radius, 0.0D)
                        .endVertex();
            //pos (DDD)Lnet/minecraft/client/renderer/BufferBuilder; func_181662_b
        }
        tessellator.end();
        GlStateManagerWrapper.color(0.0F, 0.0F, 0.0F, 0.0F);
        GlStateManagerWrapper.enableTexture2D();//	enableTexture2D ()V func_179098_w
        GlStateManagerWrapper.disableBlend();//disableBlend ()V func_179084_k
    }
    public static double roundToDecimal(double n, int point) {
        if (point == 0) {
            return Math.floor(n);
        }
        double factor = Math.pow(10, point);
        return Math.round(n * factor) / factor;
    }


}
