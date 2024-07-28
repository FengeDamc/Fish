package com.fun.client.config;


import com.fun.client.FunGhostClient;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.inject.Agent;
import com.fun.inject.Main;

import java.io.*;
import java.util.ArrayList;

public class ConfigModule {
    public Module mod;

    public ConfigModule(Module imod) {
        mod = imod;
    }

    public void save(PrintWriter writer) {
        ArrayList<Setting> settingsByMod =  FunGhostClient.settingsManager.getSettingsByMod(mod);
        writer.println(mod.getName()+"-"+"running"+":"+mod.running);
        writer.println(mod.getName()+"-"+"key"+":"+mod.key);

        for (Setting set : settingsByMod) {
                //Minecraft.getLogger().info(set.getName() + ":" + (set.isSlider() ? set.getValDouble() : set.isCheck() ? set.getValBoolean() : set.isCombo() ? set.getValString() : null));
                writer.println(set.getParentMod().getName()+"-"+set.getName() + ":" + (set.isSlider() ? set.getValDouble() : set.isCheck() ? set.getValBoolean() : set.isCombo() ? set.getValString() : null));
        }

    }
    public static void saveConfig() {
        PrintWriter writer=null;
        File file=new File(new File(Main.mcPath).getParent(),"Fish.config-"+ Agent.minecraftVersion.getVer()+".txt");
        try {
            writer=new PrintWriter(file);
        } catch (FileNotFoundException ignored) {

        }
        if (!file.exists()) {
            try {

                file.createNewFile();
            } catch (IOException ignored) {

            }
        }
        for (Module mod :FunGhostClient.registerManager.mods) {
            mod.configModule.save(writer);
        }
        if (writer != null) {
            writer.close();
        }
    }

    public static void loadConfig() {
        FileReader reader=null;
        File file=new File(new File(Main.mcPath).getParent(),"Fish.config-"+ Agent.minecraftVersion.getVer()+".txt");
        if (!file.exists()) {
            try {

                file.createNewFile();
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }//如果文件不存在及创建
        }

        try {
            reader=new FileReader(file);
        } catch (FileNotFoundException ignored) {
            ignored.printStackTrace();
        }

        try {

            BufferedReader bufferedreader = new BufferedReader(reader);
            String s = "";
            while ((s = bufferedreader.readLine()) != null) {

                String[] astring = s.split(":");

                //Minecraft.getLogger().info(mod.name);
                for(Module m:FunGhostClient.registerManager.mods) {
                    if ((m.getName() + "-" + "running").equalsIgnoreCase(astring[0])) {
                        m.setRunning(Boolean.parseBoolean(astring[1]));
                    }
                    if ((m.getName() + "-" + "key").equalsIgnoreCase(astring[0])) {
                        m.setKey(Integer.parseInt(astring[1]));
                    }
                }

                for (Setting set : FunGhostClient.settingsManager.getSettings()) {

                    if ((set.getParentMod().getName()+"-"+set.getName()).equalsIgnoreCase(astring[0])) {
                        if (set.isCombo()) {
                            set.setValString(astring[1]);
                            //Minecraft.getLogger().info(astring[1]);
                        }
                        if (set.isSlider()) {
                            set.setValDouble(Double.parseDouble(astring[1]));
                            //Minecraft.getLogger().info(astring[1]);
                        }
                        if (set.isCheck()) {
                            set.setValBoolean(Boolean.parseBoolean(astring[1]));
                            //Minecraft.getLogger().info(astring[1]);
                        }
                        //Minecraft.getLogger().info(astring);
                    }
                }

                //
            }


            //bufferedreader.close();
        } catch (IOException ignored) {
            //录入setting
            ignored.printStackTrace();
        }



        if (reader != null) {
            try {
                reader.close();
            } catch (IOException ignored) {
                //close reader
                ignored.printStackTrace();
            }
        }
    }

}
