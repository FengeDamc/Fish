package fun.client.mods.render;

import com.darkmagician6.eventapi.event.events.EventRender2D;
import fun.client.FunGhostClient;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.client.settings.Setting;
import fun.client.utils.ColorUtils;
import fun.inject.inject.wrapper.impl.gui.GuiWrapper;
import fun.inject.inject.wrapper.impl.gui.ScaledResolutionWrapper;
import fun.utils.font.FontManager;
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
    public Setting mixColor1=new Setting("MixColor1",this,0xffffff,0x000000,0xffffff,true);
    public Setting mixColor2=new Setting("MixColor2",this,0xffffff,0x000000,0xffffff,true);


    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);

            //drawRect(2, 2, 100, 14, ColorUtils.getRainbow((int) 3872.0f, (int) (10*1E7)));
        FontManager.tenacity.drawStringWithShadow("FISH", 4, 4, new Color(78, 255, 166, 235).getRGB());
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
                if (i<runningSize-1&&FontManager.tenacity.getStringWidth(m.getName())<FontManager.tenacity.getStringWidth(running.get(i+1).getName())){
                    running.set(i,running.get(i+1));
                    running.set(i+1,m);

                }
            }
        for(Module m:running){
            ScaledResolutionWrapper sr=new ScaledResolutionWrapper(mc);
            double offset = yCount * (mc.getFontRenderer().getHeight() + 6);

            if (m.running) {
                if (!modBlacklist.contains(m.getClass().getName())) {
                    int color=ColorUtils.mixColors(new Color((int) mixColor1.getValDouble()),new Color((int) mixColor2.getValDouble()),ColorUtils.getBlendFactor(new Vector2d(sr.getScaledWidth() - FontManager.tenacity.getStringWidth(m.getName())-6, (int) offset))).getRGB();
                    GuiWrapper.drawRect(sr.getScaledWidth() -4, (int) (4 + offset),sr.getScaledWidth(),(int) (4 + offset+FontManager.tenacity.getHeight()),color);
                    //GuiWrapper.drawRect(sr.getScaledWidth() - mc.getFontRenderer().getStringWidth(m.getName())-6, (int) offset, sr.getScaledWidth(), (int) (6 + mc.getFontRenderer().getHeight() + offset), new Color(255,255,200, 35).getRGB());
                    FontManager.tenacity.drawStringWithShadow( m.getName(), sr.getScaledWidth() -FontManager.tenacity.getStringWidth(m.getName())-4, (int) (4 + offset),color);
                    yCount++;
                    index++;
                    x++;
                }
            }
        }
    }
}
