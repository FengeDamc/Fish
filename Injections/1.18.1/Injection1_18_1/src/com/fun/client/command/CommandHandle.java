package com.fun.client.command;


import com.fun.client.FunGhostClient;
import com.fun.client.mods.Module;
import com.mojang.blaze3d.platform.InputConstants;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class CommandHandle {
    public static boolean onSendMessage(String st){
        String s= StringUtils.normalizeSpace(st.trim());
        System.out.println(s);
        if(s.startsWith(".")){
            if(s.startsWith(".bind")){
                String[] s1=s.split(" ");
                if(s1.length!=3) {
                    return true;
                }
                for(Module m: FunGhostClient.registerManager.mods){
                    if(s1[1].equalsIgnoreCase(m.getName())){
                        m.setKey(s1[2].equalsIgnoreCase("none")?0: InputConstants.getKey(s1[2].toUpperCase()).getValue());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
