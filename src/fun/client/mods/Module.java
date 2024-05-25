package fun.client.mods;

import com.darkmagician6.eventapi.event.events.*;
import fun.client.FunGhostClient;
import fun.client.config.ConfigModule;
import fun.client.settings.Setting;
import fun.inject.Agent;
import fun.inject.inject.wrapper.impl.MinecraftWrapper;
import fun.network.TCPClient;

import java.util.ArrayList;

public class Module {
    public int key;
    public Category category;
    public String name;
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
            if(Agent.isAgent)this.running = running;
            else TCPClient.send(Agent.SERVERPORT,"setrun "+FunGhostClient.moduleManager.mods.indexOf(this)+" "+running);
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
}
