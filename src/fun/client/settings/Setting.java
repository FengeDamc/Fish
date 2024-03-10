package fun.client.settings;


import fun.client.FunGhostClient;
import fun.client.mods.Module;

import java.util.ArrayList;
import java.util.Arrays;

// I have to delete all the comments for normal encoding

public class Setting {
	private String name;
	private Module parent;
	private String mode;

	private String sval="";
	private ArrayList<String> options;

	private boolean bval=false;

	private double dval=0.0d;
	private double min;
	private double max;
	private boolean onlyint = false;

	public Setting(String name, Module parent, String sval, ArrayList<String> options){
		this.name = name;
		this.parent = parent;
		this.sval = sval;
		this.options = options;
		this.mode = "Combo";
		FunGhostClient.settingsManager.reg(this);

	}
	public Setting(String name, Module parent, String sval, String[] options){
		this.name = name;
		this.parent = parent;
		this.sval = sval;
		this.options = new ArrayList<>();
		this.options.addAll(Arrays.asList(options));
		this.mode = "Combo";
		FunGhostClient.settingsManager.reg(this);


	}

	public Setting(String name, Module parent, boolean bval){
		this.name = name;
		this.parent = parent;
		this.bval = bval;
		this.mode = "Check";
		FunGhostClient.settingsManager.reg(this);

	}
	public boolean isAlive(){
		return true;
	}

	public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint){
		this.name = name;
		this.parent = parent;
		this.dval = dval;
		this.min = min;
		this.max = max;
		this.onlyint = onlyint;
		this.mode = "Slider";
		FunGhostClient.settingsManager.reg(this);
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
		this.sval = in;
		this.parent.onSettingChange(this);

	}

	public ArrayList<String> getOptions(){
		return this.options;
	}

	public boolean getValBoolean(){
		return this.bval;
	}

	public void setValBoolean(boolean in){
		this.bval = in;
		this.parent.onSettingChange(this);

	}

	public double getValDouble(){
		if(this.onlyint){
			this.dval = (int)dval;
		}
		return this.dval;
	}

	public void setValDouble(double in){
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
		return this.mode.equalsIgnoreCase("Combo") ? true : false;
	}

	public boolean isCheck(){
		return this.mode.equalsIgnoreCase("Check") ? true : false;
	}

	public boolean isSlider(){
		return this.mode.equalsIgnoreCase("Slider") ? true : false;
	}

	public boolean onlyInt(){
		return this.onlyint;
	}
}
