package com.fun.client.mods;

import com.fun.client.mods.render.ESP;
import com.fun.client.mods.world.Backtrack;

public class VModuleManager {
    public Backtrack backtrack;
    public ESP esp;
    public void init(){
        try{
            backtrack = new Backtrack();
            esp = new ESP();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}