package com.fun.client.settings;


import com.fun.client.FunGhostClient;
import com.fun.client.mods.Module;
import com.fun.inject.Agent;
import com.fun.network.TCPClient;
import com.fun.network.TCPServer;
import com.fun.network.packets.*;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

// I have to delete all the comments for normal encoding

public class Setting implements Serializable {
	public String name;
	public Module parent;
	public String smode;


	public String sval="";
	public boolean visible =true;
	public ArrayList<String> options;

	public boolean bval=false;

	public double dval=0.0d;
	public double min;
	public double max;
	public boolean onlyint = false;
	public ArrayList<JComponent> jComponents=new ArrayList<>();

	public Setting(String name, Module parent, String sval, ArrayList<String> options){
		this.name = name;
		this.parent = parent;
		this.sval = sval;
		this.options = options;
		this.smode = "Combo";
		FunGhostClient.settingsManager.reg(this);
		send();

	}
	public Setting(String name, Module parent, String sval, String[] options){
		this.name = name;
		this.parent = parent;
		this.sval = sval;
		this.options = new ArrayList<>();
		this.options.addAll(Arrays.asList(options));
		this.smode = "Combo";
		FunGhostClient.settingsManager.reg(this);
		send();


	}
	public void send(){
		if(Agent.isAgent)TCPClient.send(TCPServer.getTargetPort(),new PacketAddSetting(this.parent,this));
	}

	public Setting() {
		super();
	}

	public Setting(String name, Module parent, boolean bval){
		this.name = name;
		this.parent = parent;
		this.bval = bval;
		this.smode = "Check";
		FunGhostClient.settingsManager.reg(this);
		send();

	}


	public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint){
		this.name = name;
		this.parent = parent;
		this.dval = dval;
		this.min = min;
		this.max = max;
		this.onlyint = onlyint;
		this.smode = "Slider";
		FunGhostClient.settingsManager.reg(this);
		send();
	}


	public void updateActiveState(){
		if(!Agent.isAgent) {
			for (int i = 0, jComponentsSize = jComponents.size(); i < jComponentsSize; i++) {

				JComponent jc = jComponents.get(i);
				boolean b = isVisible() == jc.isVisible();
				jc.setVisible(isVisible());
				//System.out.println(jc.getName() + " " + jc.isVisible());
				if (!b) {
					SwingUtilities.invokeLater(() ->
							SwingUtilities.updateComponentTreeUI(Agent.fishFrame));
				}
			}
		}

	}

	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible){
		if(visible!=this.visible)TCPClient.send(TCPServer.getTargetPort(),new PacketSetSettingVisible(this,visible));
		this.visible=visible;
	}

	public String getName(){
		return name;
	}

	public Module getParentMod(){
		return parent;
	}

	public String getValString(){
		return this.sval.toLowerCase();
	}

	public void setValString(String in){
		if(!TCPServer.isNetworkThread()) {//FunGhostClient.settingsManager.getSettings().indexOf(this)
			TCPClient.send(TCPServer.getTargetPort(),new PacketSetString(FunGhostClient.settingsManager.getSettings().indexOf(this),in));


		}
		this.sval = in;
		this.onSettingChange();


	}

	private void onSettingChange() {
		FunGhostClient.settingsManager.onSettingChange();
		this.parent.onSettingChange(this);
	}

	public ArrayList<String> getOptions(){
		return this.options;
	}

	public boolean getValBoolean(){
		return this.bval;
	}

	public void setValBoolean(boolean in){
		if(!TCPServer.isNetworkThread()) {
			TCPClient.send(TCPServer.getTargetPort(),new PacketSetBool(FunGhostClient.settingsManager.getSettings().indexOf(this),in));

		}
		this.bval = in;
		this.parent.onSettingChange(this);

	}

	public strictfp double getValDouble(){
		if(this.onlyint){
			this.dval = (int)dval;
		}
		return this.dval;
	}

	public void setValDouble(double in){
		if(!TCPServer.isNetworkThread()) {
			TCPClient.send(TCPServer.getTargetPort(),new PacketSetDouble(FunGhostClient.settingsManager.getSettings().indexOf(this),in));
		}
		this.dval = in;
		this.parent.onSettingChange(this);

	}

	public double getMin(){
		return this.min;
	}

	public double getMax(){
		return this.max;
	}

	public boolean isCombo(){
		return this.smode.equalsIgnoreCase("Combo") ? true : false;
	}

	public boolean isCheck(){
		return this.smode.equalsIgnoreCase("Check") ? true : false;
	}

	public boolean isSlider(){
		return this.smode.equalsIgnoreCase("Slider") ? true : false;
	}

	public boolean onlyInt(){
		return this.onlyint;
	}
}
