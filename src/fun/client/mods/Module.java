package fun.client.mods;

import com.darkmagician6.eventapi.EventManager;
import fun.client.FunGhostClient;
import fun.client.settings.Setting;

public class Module {
    public int key;
    public Category category;
    public String name;
    public Module(int keyIn,String nameIn,Category categoryIn){
        key=keyIn;
        name=nameIn;
        FunGhostClient.moduleManager.mods.add(this);
        EventManager.register(this);
        category=categoryIn;

    }
    public Module(String nameIn,Category category){
       this(0,nameIn,category);

    }
    public void onSettingChange(Setting setIn){

    }
}
