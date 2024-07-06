package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Module;
import com.fun.client.mods.ModuleManager;
import com.fun.client.settings.Setting;
import com.fun.client.settings.SettingsManager;
import com.fun.inject.Agent;
import com.fun.inject.InjectorUtils;
import com.fun.inject.Main;
import com.fun.inject.MinecraftType;
import com.fun.inject.mapper.Mapper;
import com.fun.network.IPacket;
import com.fun.network.IReviewable;

import java.io.File;
import java.util.ArrayList;

public class PacketInit implements IPacket{


    public PacketInit() {
        super();

    }

    @Override
    public void process() {
        File injection= Mapper.mapJar(new File(new File(Main.path),"/injections/"+ Agent.minecraftVersion.injection), MinecraftType.NONE);
        System.out.println("injection: "+injection.getAbsolutePath());
        InjectorUtils.addToSystemClassLoaderSearch(injection.getAbsolutePath());
        FunGhostClient.moduleManager=new ModuleManager();
        FunGhostClient.settingsManager=new SettingsManager();


    }

}
