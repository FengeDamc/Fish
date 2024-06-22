package com.fun.gui;

import com.fun.inject.inject.InjectUtils;
import com.fun.inject.inject.WindowEnumerator;

public class Main {
    public static FishFrame fishFrame=null;
    public static void main(String[] args) {
        //FunGhostClient.init();
        try {
            int pid=(InjectUtils.getMinecraftProcessId());
            for(String s: WindowEnumerator.getWindows(pid)){
                System.out.println(s);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
