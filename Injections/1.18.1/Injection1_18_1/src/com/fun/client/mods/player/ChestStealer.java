package com.fun.client.mods.player;

import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.client.settings.Setting;

public class ChestStealer extends VModule {
    public ChestStealer() {
        super("ChestStealer", Category.Player);
    }
    public Setting scaffoldBlocks=new Setting("ScaffoldBlocks",this,true);
    public Setting swords=new Setting("Swords",this,true);
    public Setting axe=new Setting("Axe",this,true);
    public Setting shovel=new Setting("Shovel",this,true);
    public Setting hoe=new Setting("Hoe",this,true);
    public Setting food=new Setting("Foods",this,true);
    public Setting armor=new Setting("Armor",this,true);
    public Setting drinks=new Setting("Drinks",this,true);

}
