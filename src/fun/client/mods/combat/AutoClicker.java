package fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventUpdate;
import com.sun.jna.platform.win32.WinNT;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.client.settings.Setting;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.awt.event.InputEvent;

public class AutoClicker extends Module {
    public Robot robot;
    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public Setting lcps =new Setting("LCPS",this,12,0,20,true);
    public Setting rcps=new Setting("RCPS",this,12,0,20,true);

    public Setting left=new Setting("Left",this,true);
    public Setting right=new Setting("Right",this,true);


    public AutoClicker() {
        super("AutoClicker",Category.Combat);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);
        if(Math.random()< lcps.getValDouble()/20){
            if(mc.getGameSettings().getKey("key.attack").isPressed()&&left.getValBoolean()){
                mc.clickMouse();


            }



        }
        if(Math.random()< rcps.getValDouble()/20)
            if(mc.getGameSettings().getKey("key.use").isPressed()&&right.getValBoolean())
                if(!mc.getPlayer().isUsingItem()){
                    mc.rightClickMouse();//MD: wn/bS ()Z net/minecraft/entity/player/EntityPlayer/func_71039_bw ()Z
                }

    }
}
