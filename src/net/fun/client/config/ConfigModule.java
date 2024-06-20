package net.fun.client.config;


import net.fun.client.FunGhostClient;
import net.fun.client.mods.Module;
import net.fun.client.settings.Setting;
import net.fun.inject.Main;

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
        File file=new File(Main.mcPath+"/Fish.config.txt");
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
        for (Module mod :FunGhostClient.moduleManager.mods) {
            mod.configModule.save(writer);
        }
        if (writer != null) {
            writer.close();
        }
    }

    public static void loadConfig() {
        FileReader reader=null;
        File file=new File(Main.mcPath+"/Fish.config.txt");
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
                for(Module m:FunGhostClient.moduleManager.mods) {
                    if ((m.getName() + "-" + "running").equalsIgnoreCase(astring[0])) {
                        m.running=Boolean.parseBoolean(astring[1]);
                    }
                    if ((m.getName() + "-" + "key").equalsIgnoreCase(astring[0])) {
                        m.key= Integer.parseInt(astring[1]);
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
