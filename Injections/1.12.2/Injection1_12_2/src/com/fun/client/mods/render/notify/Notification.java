package com.fun.client.mods.render.notify;

import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;
import com.fun.inject.injection.wrapper.impl.gui.ScaledResolutionWrapper;
import com.fun.client.font.FontManager;
import com.fun.utils.render.RenderUtils;

import javax.vecmath.Vector2f;
import java.awt.*;

public class Notification {
    public enum Type{
        GREEN(new Color(0x739CFFCC, true)),
        RED(new Color(0x73FF5555, true)),
        WHITE(new Color(255,255,255,100));
        public final Color color;
        Type(Color c){
            color=c;
        }
    }
    public int width=100;
    public long tick=System.currentTimeMillis()+(1000*5);
    public int height=25;
    public MinecraftWrapper mc=MinecraftWrapper.get();
    public ScaledResolutionWrapper scaledResolution=new ScaledResolutionWrapper(mc);
    public Vector2f currentPos=new Vector2f(scaledResolution.getScaledWidth()+width,scaledResolution.getScaledHeight()+height);
    public Vector2f targetPos=currentPos;

    public float speed=1f;
    public String text;
    public Type type;

    public Notification(String text, Type type) {
        super();
        this.text=text;
        this.type=type;
    }

    public Notification(String text) {
        this(text, Type.GREEN);
    }
    public void setTargetPos(Vector2f targetPos){
        this.targetPos=targetPos;

    }
    public void tick(){
        currentPos=limitPos(currentPos,targetPos);
    }
    public void render(){


        RenderUtils.drawRoundedRect((int)currentPos.x-width,(int)currentPos.y-height,(int)currentPos.x,(int)currentPos.y,5,this.type.color.getRGB());
        FontManager.tenacity20.drawStringWithShadow(text,(int)currentPos.x-width,(int)currentPos.y-height+3,Color.WHITE.getRGB());
    }
    public static Vector2f limitPos(final Vector2f currentRotation, final Vector2f targetRotation) {
        float xDifference =  targetRotation.x-currentRotation.x;
        float yDifference =  targetRotation.y-currentRotation.y;

        return new Vector2f((float) (currentRotation.x+xDifference*0.6),
                (float) (currentRotation.y+yDifference*0.6));

    }

}
