package net.fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventTick;
import net.fun.client.mods.Category;
import net.fun.client.mods.Module;
import net.fun.client.settings.Setting;
import net.fun.inject.inject.wrapper.impl.setting.KeyBindingWrapper;
import net.fun.utils.Fields;
import net.fun.utils.Methods;

public class AutoClicker extends Module {
    //#define MOUSEEVENTF_LEFTDOWN 0x0002
    //#define MOUSEEVENTF_LEFTUP 0x0004
    //#define MOUSEEVENTF_RIGHTDOWN 0x0008
    //#define MOUSEEVENTF_RIGHTUP 0x0010
    public static final int MOUSEEVENTF_LEFTDOWN = 0x0002;
    public static final int MOUSEEVENTF_LEFTUP = 0x0004;
    public static final int MOUSEEVENTF_RIGHTDOWN = 0x0008;
    public static final int MOUSEEVENTF_RIGHTUP = 0x0010;





    public Setting lcps =new Setting("LCPS",this,12,0,20,true);
    public Setting rcps=new Setting("RCPS",this,12,0,20,true);

    public Setting left=new Setting("Left",this,true);
    public Setting right=new Setting("Right",this,true);


    public AutoClicker() {
        super("AutoClicker",Category.Combat);
    }

    @Override
    public void onTick(EventTick event) {
        super.onTick(event);
        if(Math.random()< lcps.getValDouble()/20){
            if(mc.getGameSettings().getKey("key.attack").isPressed()&&left.getValBoolean()){
                //NativeUtils.clickMouse(MOUSEEVENTF_LEFTDOWN);
                //NativeUtils.clickMouse(MOUSEEVENTF_LEFTUP);
                sendClick(mc.getGameSettings().getKey("key.attack"),true);
                //sendClick(0,false);


            }



        }
        if(Math.random()< rcps.getValDouble()/20)
            if(mc.getGameSettings().getKey("key.use").isPressed()&&right.getValBoolean())
                if(!mc.getPlayer().isUsingItem()){
                    sendClick(mc.getGameSettings().getKey("key.use"),true);
                    //sendClick(1,false);//tClickMouse();//MD: wn/bS ()Z net/minecraft/entity/player/EntityPlayer/func_71039_bw ()Z
                }
    }

    public void sendClick(final KeyBindingWrapper button, final boolean state) {
        final int keyBind = button.getKeyCode();//KeyBinding.setKeyBindState(keyBind, state); func_74510_a
        Fields.pressTime_KeyBinding.set(button.keyBindingObj,0);
        Methods.setKeyBindState_KeyBinding.invoke(null,keyBind,state);
        if (state) {
            Methods.onTick_KeyBinding.invoke(null,keyBind);//KeyBinding.onTick(keyBind);
        }
    }
}
