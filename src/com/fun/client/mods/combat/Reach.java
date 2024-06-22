package com.fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventAttackReach;
import com.darkmagician6.eventapi.event.events.EventBlockReach;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import org.lwjgl.input.Keyboard;

public class Reach extends Module {
    public Setting attackRanga=new Setting("AttackRange",this,6.0,3.0,6.0,false);
    public Setting blockRanga=new Setting("BlockRange",this,6.0,3.0,6.0,false);

    public Reach() {
        super(Keyboard.KEY_G,"Reach", Category.Combat);
    }

    @Override
    public void onAttackReach(EventAttackReach event) {
        super.onAttackReach(event);
        event.reach=attackRanga.getValDouble();
    }

    @Override
    public void onBlockReach(EventBlockReach event) {
        super.onBlockReach(event);
        event.reach=blockRanga.getValDouble();
    }
}
