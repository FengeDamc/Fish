package fun.client;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.event.events.EventRender3D;
import fun.client.mods.ModuleManager;
import fun.client.settings.Setting;
import fun.client.settings.SettingsManager;

import java.util.ArrayList;

public class FunGhostClient {
    public static SettingsManager settingsManager=new SettingsManager();
    public static ModuleManager moduleManager=new ModuleManager();
    public static void init(){
            try{
                moduleManager.init();
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }
    public static void onRender3D(float f){
        EventManager.call(new EventRender3D((f)));
    }

}
