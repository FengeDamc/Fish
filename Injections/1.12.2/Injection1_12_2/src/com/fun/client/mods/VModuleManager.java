package com.fun.client.mods;

import com.fun.client.mods.world.Backtrack;

public class VModuleManager {
    public Backtrack backtrack;
    public void init(){
        try{
            backtrack = new Backtrack();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
