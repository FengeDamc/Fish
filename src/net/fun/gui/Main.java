package net.fun.gui;

import net.fun.client.FunGhostClient;
import net.fun.inject.Agent;
import net.fun.inject.inject.InjectUtils;
import net.fun.inject.inject.WindowEnumerator;
import sun.awt.Win32GraphicsEnvironment;

import javax.swing.*;

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
