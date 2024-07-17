package com.fun.client.mods;

import com.fun.eventapi.event.events.*;
import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.client.settings.Setting;
import com.fun.inject.Agent;
import com.fun.inject.injection.wrapper.impl.MinecraftWrapper;
import com.fun.network.TCPClient;
import com.fun.network.TCPServer;
import com.fun.network.packets.PacketAddModule;
import com.fun.network.packets.PacketSetKey;
import com.fun.network.packets.PacketSetRun;
import com.fun.client.mods.render.notify.Notification;

import java.io.Serializable;
import java.util.ArrayList;

public class Module{
    public int key;
    public Category category;
    public String name;

    public MinecraftWrapper mc;
    public ConfigModule configModule =null;

    public boolean running=false;
    public Module(int keyIn,String nameIn,Category categoryIn){
        key=keyIn;
        name=nameIn;
        FunGhostClient.moduleManager.mods.add(this);
        //EventManager.register(this);
        category=categoryIn;
        configModule=new ConfigModule(this);
        if(Agent.isAgent){
            mc=MinecraftWrapper.get();
            TCPClient.send(TCPServer.getTargetPort(),new PacketAddModule(this));
        }
    }

    public Module() {
        super();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        if(running!=this.running) {
            this.running = running;
            FunGhostClient.onValueChange();
            if(Agent.isAgent){
                if (running) onEnable();
                else onDisable();
            }
            if (!TCPServer.isNetworkThread()) {
                TCPClient.send(TCPServer.getTargetPort(), new PacketSetRun(FunGhostClient.moduleManager.mods.indexOf(this),running));
            }
        }
    }
    public void setKey(int key){
        this.key=key;
        if (!TCPServer.isNetworkThread()) {
            TCPClient.send(TCPServer.getTargetPort(), new PacketSetKey(FunGhostClient.moduleManager.mods.indexOf(this),key));
        }
    }

    public String getName() {
        return name;
    }

    public Module(String nameIn, Category category){
       this(0,nameIn,category);

    }
    public void onSettingChange(Setting setIn){

    }

    public void onUpdate(EventUpdate event) {
        //Agent.System.out.println(this);
    }

    public void onPacket(EventPacket packet) {
    }

    public void onKey(EventKey event) {
    }

    public void onRender3D(EventRender3D event) {
    }

    public void onRender2D(EventRender2D event) {
    }

    public void onBlockReach(EventBlockReach event) {
    }

    public void onAttackReach(EventAttackReach event) {
    }
    public ArrayList<Setting> getSettings(){
        return FunGhostClient.settingsManager.getSettingsByMod(this);
    }

    public void onStrafe(EventStrafe event) {
    }

    public void onMotion(EventMotion event) {
    }

    public void onMoment(EventMoment event) {
    }

    public void onTick(EventTick event) {
    }
    public void onEnable(){
        FunGhostClient.moduleManager.notification.post(new Notification("Enable "+getName(), Notification.Type.GREEN));

    }
    public void onDisable(){
        FunGhostClient.moduleManager.notification.post(new Notification("Disable "+getName(), Notification.Type.RED));
    }
}
