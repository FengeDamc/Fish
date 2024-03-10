package fun.client.mods;

import fun.client.mods.moment.Fly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class ModuleManager{
    public ArrayList<Module> mods;

    public Fly fly;
    public void init(){
       fly=new Fly("Fly");
    }

}
