package fun.client;

import fun.client.mods.ModuleManager;
import fun.client.settings.Setting;
import fun.client.settings.SettingsManager;

import java.util.ArrayList;

public class FunGhostClient {
    public static SettingsManager settingsManager=new SettingsManager();
    public static ModuleManager moduleManager=new ModuleManager();
    public static void init(){
        moduleManager.mods=new ArrayList<>();;
        moduleManager.init();
    }
}
