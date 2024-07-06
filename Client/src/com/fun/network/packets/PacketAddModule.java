package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.network.IPacket;

public class PacketAddModule implements IPacket {
    public String name;
    public int key;
    public int category;

    public PacketAddModule(Module m) {
        super();
        this.name=m.name;
        this.key=m.key;
        this.category= m.category.ordinal();
    }

    @Override
    public void process() {
        System.out.println(name+" "+key+" "+category);
        Module m=new Module();
        m.name=name;
        m.key=key;
        m.category=Category.values()[category];
        m.configModule=new ConfigModule(m);
        FunGhostClient.moduleManager.mods.add(m);//key,name,Category.values()[category]
    }
}
