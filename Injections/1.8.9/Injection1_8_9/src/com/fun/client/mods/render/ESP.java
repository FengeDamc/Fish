package com.fun.client.mods.render;

import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.eventapi.event.events.EventRender2D;
import com.fun.inject.Agent;
import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;
import com.fun.inject.mapper.SideOnly;
import com.fun.utils.math.MathUtils;
import com.fun.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;


import static org.lwjgl.opengl.GL11.*;


public class ESP extends VModule{


    @SideOnly(SideOnly.Type.AGENT)

    private static FloatBuffer windPos;
    @SideOnly(SideOnly.Type.AGENT)

    private static IntBuffer intBuffer;
    @SideOnly(SideOnly.Type.AGENT)

    private static FloatBuffer floatBuffer1;
    @SideOnly(SideOnly.Type.AGENT)

    private static FloatBuffer floatBuffer2;
    @SideOnly(SideOnly.Type.AGENT)
    public void setup(){

        windPos = BufferUtils.createFloatBuffer(4);
        intBuffer = GLAllocation.createDirectIntBuffer(16);
        floatBuffer1 = GLAllocation.createDirectFloatBuffer(16);
        floatBuffer2 = GLAllocation.createDirectFloatBuffer(16);
        mc = Minecraft.getMinecraft();
    }
    public ESP() {
        super("ESP", Category.Render);
        if(Agent.isAgent) setup();
    }
    @SideOnly(SideOnly.Type.AGENT)

    public static double[] getInterpolatedPos(Entity entity) {
        float ticks = MinecraftWrapper.get().getTimer().getRenderPartialTicks();
        return new double[]{
                MathUtils.interpolate(entity.lastTickPosX, entity.posX, ticks) - mc.getRenderManager().viewerPosX,
                MathUtils.interpolate(entity.lastTickPosY, entity.posY, ticks) - mc.getRenderManager().viewerPosY,
                MathUtils.interpolate(entity.lastTickPosZ, entity.posZ, ticks) - mc.getRenderManager().viewerPosZ
        };
    }
    @SideOnly(SideOnly.Type.AGENT)

    public static AxisAlignedBB getInterpolatedBoundingBox(Entity entity) {
        final double[] renderingEntityPos = getInterpolatedPos(entity);
        final double entityRenderWidth = entity.width / 1.7;
        return new AxisAlignedBB(renderingEntityPos[0] - entityRenderWidth,
                renderingEntityPos[1], renderingEntityPos[2] - entityRenderWidth, renderingEntityPos[0] + entityRenderWidth,
                renderingEntityPos[1] + entity.height + (entity.isSneaking() ? -0.3 : 0.18), renderingEntityPos[2] + entityRenderWidth).expand(0.15, 0.15, 0.15);
    }
    @SideOnly(SideOnly.Type.AGENT)

    public static Minecraft mc;
    @SideOnly(SideOnly.Type.AGENT)

    public static Vector4f getEntityPositionsOn2D(Entity entity) {
        final AxisAlignedBB bb = getInterpolatedBoundingBox(entity);

        final List<Vector3f> vectors = Arrays.asList(
                new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.minZ),
                new Vector3f((float) bb.minX, (float) bb.maxY, (float) bb.minZ),
                new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.minZ),
                new Vector3f((float) bb.maxX, (float) bb.maxY, (float) bb.minZ),
                new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.maxZ),
                new Vector3f((float) bb.minX, (float) bb.maxY, (float) bb.maxZ),
                new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.maxZ),
                new Vector3f((float) bb.maxX, (float) bb.maxY, (float) bb.maxZ));

        Vector4f entityPos = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1.0f, -1.0f);
        ScaledResolution sr = new ScaledResolution(mc);
        for (Vector3f vector3f : vectors) {
            vector3f = projectOn2D(vector3f.x, vector3f.y, vector3f.z, sr.getScaleFactor());
            if (vector3f != null && vector3f.z >= 0.0 && vector3f.z < 1.0) {
                entityPos.x = Math.min(vector3f.x, entityPos.x);
                entityPos.y = Math.min(vector3f.y, entityPos.y);
                entityPos.z = Math.max(vector3f.x, entityPos.z);
                entityPos.w = Math.max(vector3f.y, entityPos.w);
            }
        }
        return entityPos;
    }
    @SideOnly(SideOnly.Type.AGENT)

    public static Vector3f projectOn2D(float x, float y, float z, int scaleFactor) {
        glGetFloat(GL_MODELVIEW_MATRIX, floatBuffer1);
        glGetFloat(GL_PROJECTION_MATRIX, floatBuffer2);

        glGetInteger(GL_VIEWPORT, intBuffer);
        if (GLU.gluProject(x, y, z, floatBuffer1, floatBuffer2, intBuffer, windPos)) {
            return new Vector3f(windPos.get(0) / scaleFactor, (mc.displayHeight - windPos.get(1)) / scaleFactor, windPos.get(2));
        }
        return null;
    }
    @SideOnly(SideOnly.Type.AGENT)

    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        for(Entity e:mc.theWorld.getLoadedEntityList()){
            //Vector4f v=getEntityPositionsOn2D(e);
            //RenderUtils.drawRoundedRect((int) v.x, (int) v.y, (int) v.z, (int) v.w,5,
                    //);
            AxisAlignedBB aabb=e.getCollisionBoundingBox();
            Color color=e instanceof EntityPlayer ? Color.PINK : Color.lightGray;
            com.fun.utils.v189.RenderUtils.glColor(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
            RenderUtils.drawBoundingBox(new com.fun.utils.math.AxisAlignedBB(aabb.minX,aabb.minY,aabb.minZ,
                    aabb.maxX,aabb.maxY,aabb.maxZ));//
        }
    }
}
