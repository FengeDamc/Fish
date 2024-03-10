package fun.client.mods.moment;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.EventUpdate;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.inject.Agent;

public class Fly extends Module {
    public Fly(String nameIn) {
        super(nameIn, Category.Movement);
    }
    @EventTarget
    public void onUpdate(EventUpdate event){
        Agent.logger.info("update");
    }

}
