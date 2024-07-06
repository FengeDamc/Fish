package com.fun.network.packets;

import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.gui.FishFrame;
import com.fun.inject.Agent;
import com.fun.inject.InjectorUtils;
import com.fun.inject.Main;
import com.fun.inject.MinecraftType;
import com.fun.inject.mapper.Mapper;
import com.fun.network.IPacket;

import java.io.File;

public class PacketMCPath implements IPacket
{
    public String path;

    public PacketMCPath(String path) {
        this.path = path;
    }

    @Override
    public void process() {
        try {
            Main.mcPath = path;
            Main.started = true;

            //FunGhostClient.init();

            //ConfigModule.loadConfig();

            FishFrame.init0();

            System.out.println("fishgc Started");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
