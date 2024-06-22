package com.fun.client;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.event.events.EventRender3D;
import com.fun.client.mods.ModuleManager;
import com.fun.client.settings.SettingsManager;
import com.fun.inject.inject.asm.api.Transformers;

public class FunGhostClient {
    public static SettingsManager settingsManager;
    public static ModuleManager moduleManager;
    public static void init(){
            try{
                settingsManager=new SettingsManager();
                moduleManager=new ModuleManager();
                moduleManager.init();
            }
            catch (Exception e){
                e.printStackTrace();
            }
    }
    public static void destroyClient() {
        moduleManager.mods.clear();
        settingsManager.getSettings().clear();
        Transformers.transformers.clear();
    }
    public static void onRender3D(float f){
        EventManager.call(new EventRender3D((f)));
    }

}
