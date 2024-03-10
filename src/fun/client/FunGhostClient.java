package fun.client;

import fun.client.mods.ModuleManager;
import fun.client.settings.Setting;
import fun.client.settings.SettingsManager;

public class FunGhostClient {
    public static SettingsManager settingsManager=new SettingsManager();
    public static ModuleManager moduleManager=new ModuleManager();
    public static void init(){
        moduleManager.init();
    }
}
