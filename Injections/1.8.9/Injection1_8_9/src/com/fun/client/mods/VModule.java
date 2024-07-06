package com.fun.client.mods;

import com.fun.inject.Agent;
import com.fun.inject.mapper.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.BufferUtils;

public class VModule extends Module {
    public Minecraft mc;
    public VModule(String nameIn, Category category) {
        super(nameIn, category);
        if(Agent.isAgent)mc = Minecraft.getMinecraft();

    }
    @SideOnly(SideOnly.Type.AGENT)
    public void setup(){
    }

    public VModule(int keyIn, String nameIn, Category categoryIn) {
        super(keyIn, nameIn, categoryIn);
    }
}
