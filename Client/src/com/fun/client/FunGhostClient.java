package com.fun.client;

import com.fun.client.mods.RegisterManager;
import com.fun.client.settings.Setting;
import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventRender3D;
import com.fun.client.settings.SettingsManager;
import com.fun.inject.injection.asm.api.Transformers;
import com.fun.client.mods.render.notify.Notification;
import com.fun.utils.rotation.RotationManager;
import com.fun.utils.rotation.RotationUtils;

public class FunGhostClient {
    public static SettingsManager settingsManager;
    public static RegisterManager registerManager;
    public static RotationManager rotationManager;
    public static void init(){
            try{
                rotationManager = new RotationManager();

                settingsManager=new SettingsManager();


                registerManager =new RegisterManager();


                registerManager.init();


                registerManager.vModuleManager.notification.post(new Notification("Fish Injected", Notification.Type.WHITE));


            }
            catch (Exception e){
                e.printStackTrace();
            }
    }
    public static void onValueChange(){
        for(Setting s: FunGhostClient.settingsManager.settings){
            s.setVisible(s.isVisible());
        }
    }
    public static void destroyClient() {
        registerManager.mods.clear();
        settingsManager.getSettings().clear();
        Transformers.transformers.clear();
    }
    public static void onRender3D(float f){
        EventManager.call(new EventRender3D((f)));

    }

}
