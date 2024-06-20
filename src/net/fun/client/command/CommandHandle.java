package net.fun.client.command;


import net.fun.client.FunGhostClient;
import net.fun.client.mods.Module;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

public class CommandHandle {
    public static boolean onSendMessage(String st){
        String s= StringUtils.normalizeSpace(st.trim());
        System.out.println(s);
        if(s.startsWith(".")){
            if(s.startsWith(".bind")){
                String[] s1=s.split(" ");
                if(s1.length!=3){
                    return true;
                }
                for(Module m: FunGhostClient.moduleManager.mods){

                    if(s1[1].equalsIgnoreCase(m.getName())){
                        m.setKey(s1[2].equalsIgnoreCase("none")?0: Keyboard.getKeyIndex(s1[2].toUpperCase()));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
