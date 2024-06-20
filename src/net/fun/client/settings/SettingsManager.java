package net.fun.client.settings;


import net.fun.client.FunGhostClient;
import net.fun.client.mods.Module;

import java.util.ArrayList;

// I have to delete all the comments for normal encoding

public class SettingsManager {
	private ArrayList<Setting> settings;

	public SettingsManager(){
		this.settings = new ArrayList<Setting>();

	}

	public void rSetting(Setting in){
			//this.settings.add(in);
	}
	public void reg(Setting in){
		this.settings.add(in);
	}

	public ArrayList<Setting> getSettings(){
		return this.settings;
	}

	public ArrayList<Setting> getSettingsByMod(Module mod){
		ArrayList<Setting> out = new ArrayList<Setting>();
		for(Setting s : getSettings()){
			if(s.getParentMod().equals(mod)){
				out.add(s);
			}
		}

		return out;
	}

	public Setting getSettingByName(String name){
		for(Setting set : getSettings()){
			if(set.getName().equalsIgnoreCase(name)){
				return set;
			}
		}
		System.err.println("["+ "FengeGG" + "] Error Setting NOT found: '" + name +"'!");
		return null;
	}
}
