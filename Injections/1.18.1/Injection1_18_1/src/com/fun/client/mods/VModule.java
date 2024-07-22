package com.fun.client.mods;

import com.fun.inject.Agent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.gui.ForgeIngameGui;

public class VModule extends Module {
    public Minecraft mc=Minecraft.getInstance();;
    public VModule(String nameIn, Category category) {
        super(nameIn, category);

    }

    public VModule(int keyIn, String nameIn, Category categoryIn) {
        super(keyIn, nameIn, categoryIn);
    }
}
