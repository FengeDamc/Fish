package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.network.IPacket;

public class PacketAddSetting implements IPacket {
    public PacketAddSetting(Module m,Setting s){
        this.mod= FunGhostClient.moduleManager.mods.indexOf(m);
        if(s.isSlider()){
            this.set=new Setting();//s.name,null,s.dval,s.min,s.max,s.onlyint
            this.set.name=s.name;this.set.dval=s.dval;this.set.min=s.min;this.set.max=s.max;this.set.onlyint=s.onlyint;
        }
        else if(s.isCheck()){
            this.set=new Setting();//s.name,null,s.bval
            this.set.name=s.name;this.set.bval=s.bval;

        }
        else{
            this.set=new Setting();//s.name,null,s.sval,s.options
            this.set.name=s.name;this.set.sval=s.sval;this.set.options=s.options;

        }
        this.set.smode=s.smode;

    }
    public int mod;
    public Setting set;
    @Override
    public void process() {
        set.parent=FunGhostClient.moduleManager.mods.get(mod);
        FunGhostClient.settingsManager.reg(set);
    }
}
