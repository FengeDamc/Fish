package com.fun.client.mods;

import com.darkmagician6.eventapi.event.events.*;
import com.fun.client.FunGhostClient;
import com.fun.client.config.ConfigModule;
import com.fun.client.settings.Setting;
import com.fun.inject.Agent;
import com.fun.inject.inject.wrapper.impl.MinecraftWrapper;
import com.fun.network.TCPClient;
import com.fun.network.TCPServer;
import com.fun.utils.render.Notification;

import java.util.ArrayList;

public class Module {
    public int key;
    public Category category;
    public String name;
    public int toggleTick=0;
    public MinecraftWrapper mc=MinecraftWrapper.get();
    public ConfigModule configModule =null;

    public boolean running=false;
    public Module(int keyIn,String nameIn,Category categoryIn){
        key=keyIn;
        name=nameIn;
        FunGhostClient.moduleManager.mods.add(this);
        //EventManager.register(this);
        category=categoryIn;
        configModule=new ConfigModule(this);

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        if(running!=this.running) {
            this.running = running;
            if(Agent.isAgent){
                if (running) onEnable();
                else onDisable();
            }
            if (!TCPServer.isNetworkThread()) {
                TCPClient.send(TCPServer.getTargetPort(), "setrun " + FunGhostClient.moduleManager.mods.indexOf(this) + " " + running);
            }
        }
    }
    public void setKey(int key){
        this.key=key;
        if (!TCPServer.isNetworkThread()) {
            TCPClient.send(TCPServer.getTargetPort(), "setkey " + FunGhostClient.moduleManager.mods.indexOf(this) + " " + key);
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
        //Agent.logger.info(this);
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
