package net.fun.client.mods;



import java.util.ArrayList;

public enum Category {
    Combat,
    None,
    Movement,
    MISC,
    RENDER,
    Player,
    World,
    CLIENT
    ;
    public ArrayList<Module> mods = new ArrayList<Module>();
    public void registerModule(Module m){
        getMods().add(m);
    }


    public ArrayList<Module> getMods() {
        return mods;
    }

    public void setMods(ArrayList<Module> mods) {
        this.mods = mods;
    }


}
