package fun.client.mods.render;

import com.darkmagician6.eventapi.event.events.EventRender2D;
import fun.client.FunGhostClient;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.client.settings.Setting;
import fun.client.utils.ColorUtils;
import fun.inject.Agent;
import fun.inject.inject.wrapper.impl.MinecraftWrapper;
import fun.inject.inject.wrapper.impl.gui.GuiWrapper;
import fun.inject.inject.wrapper.impl.gui.ScaledResolutionWrapper;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2d;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;



public class HUD extends Module {
    public HUD(String nameIn) {
        super(Keyboard.KEY_F,nameIn, Category.RENDER);
        this.running=true;
    }
    public Setting setting=new Setting("Test",this,"A",new String[]{"A","B","C"});

    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);

            //drawRect(2, 2, 100, 14, ColorUtils.getRainbow((int) 3872.0f, (int) (10*1E7)));
        mc.getFontRenderer().drawStringShadow("FISH", 4, 4, ColorUtils.getRainbow((int) 3872.0f, (int) (0xffffff*1E7)),false);
            //fm.arraylist.drawString("FPS: " + , 4, 25, Color.white.getRGB());
        renderArrayList();
            //FengeGG.onRenderer();


    }
    private HashSet<String> modBlacklist = new HashSet<>();

    private void renderArrayList() {
        int yCount = 0;
        int index = 0;
        long x = 0;
        ArrayList<Module> mods = FunGhostClient.moduleManager.mods;
        ArrayList<Module> running=new ArrayList<Module>();

        for (Module m : mods) {
            if (m.running)
                running.add(m);


        }
        for(Module mod:running)
            for (int i = 0, runningSize = running.size(); i < runningSize; i++) {
                Module m = running.get(i);
                if (i<runningSize-1&&mc.getFontRenderer().getStringWidth(m.getName())<mc.getFontRenderer().getStringWidth(running.get(i+1).getName())){
                    running.set(i,running.get(i+1));
                    running.set(i+1,m);

                }
            }
        for(Module m:running){
            ScaledResolutionWrapper sr=new ScaledResolutionWrapper(mc);
            double offset = yCount * (mc.getFontRenderer().getHeight() + 6);

            if (m.running) {
                if (!modBlacklist.contains(m.getClass().getName())) {
                    //GuiWrapper.drawRect(sr.getScaledWidth() - mc.getFontRenderer().getStringWidth(m.getName())-6, (int) offset, sr.getScaledWidth(), (int) (6 + mc.getFontRenderer().getHeight() + offset), new Color(255,255,200, 35).getRGB());
                    mc.getFontRenderer().drawStringShadow( m.getName(), sr.getScaledWidth() -mc.getFontRenderer().getStringWidth(m.getName()), (int) (4 + offset),
                                    ColorUtils.mixColors(new Color(255, 252, 0,255),new Color(78, 255, 166, 235),ColorUtils.getBlendFactor(new Vector2d(sr.getScaledWidth() - mc.getFontRenderer().getStringWidth(m.getName())-6, (int) offset))).getRGB(),
                            false);
                    yCount++;
                    index++;
                    x++;
                }
            }
        }
    }
}
