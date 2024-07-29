package com.fun.client.mods.player;

import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.client.settings.Setting;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.utils.math.Timer;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.*;

import static net.minecraft.world.inventory.ClickType.QUICK_MOVE;

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
    public Setting delay=new Setting("Delay",this,175,0,300,true);
    public Timer timer=new Timer();
    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);
        if (mc.player.containerMenu instanceof ChestMenu chest) {
            Map<Integer, ItemStack> itemStackMap=new HashMap<>();
            for(int i=0;i<chest.getContainer().getContainerSize();i++){
                Slot slot = chest.getSlot(i);
                if(slot.hasItem())itemStackMap.put(i, slot.getItem());
            }
            Set<Integer> set=itemStackMap.keySet();
            List<Object> list=Arrays.asList(set.toArray());
            Collections.shuffle(list);
            if(timer.passedMs((long) ((long) delay.getValDouble()+Math.random()*50L-25))){
                mc.gameMode.handleInventoryMouseClick(chest.containerId,(int)list.get(0),0, QUICK_MOVE, mc.player);
            }
            //ItemStack itemStack=chest.getSlot((Integer) list.get(0)).getItem();

        }
    }
}
