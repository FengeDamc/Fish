package fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventStrafe;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.client.settings.Setting;
import fun.inject.inject.Mappings;
import fun.inject.inject.wrapper.impl.entity.EntityPlayerSPWrapper;
import fun.inject.inject.wrapper.impl.entity.EntityWrapper;

import javax.security.auth.login.AppConfigurationEntry;

public class Target extends Module {
    public EntityWrapper target=null;
    public Setting onlyPlayer=new Setting("OnlyPlayer",this,false);
    public Setting range=new Setting("Range",this,6.0,0,6.0,false);


    @Override
    public void onDisable() {
        super.onDisable();
        target=null;
    }

    @Override
    public void onStrafe(EventStrafe event) {
        super.onStrafe(event);
        target=null;
        EntityPlayerSPWrapper playersp = mc.getPlayer();

        //playersp.setPitch(0);


        //playersp.setYaw(0);
        //Agent.logger.info(mc.getWorld().worldObj);
        double dist=Double.MAX_VALUE;
        for (EntityWrapper player : onlyPlayer.getValBoolean()?
                mc.getWorld().getLoadedPlayers()
                :mc.getWorld().getLoadedEntities()) {
            double d1=mc.getPlayer().getDistance(player);
            if (player.entityObj!=playersp.getPlayerObj()&&d1< range.getValDouble()&&d1<dist&&!player.isDead()
                    &&isEntityLivingBase(player.entityObj)) {
                target = player;
                dist=d1;
                //Agent.logger.info(+":{}",);
            }
        }

    }

    public Target() {
        super("Target",Category.Combat);

    }
    public boolean isEntityLivingBase(Object instance){
        Class<?> c = instance.getClass();
        while (c.getSuperclass() != null) {
            //Agent.logger.info(c.getName());
            if(c.getName().equals(Mappings.getObfClass("net/minecraft/entity/EntityLivingBase").replace('/','.')))
                return true;
            c = c.getSuperclass();
        }
        return false;
    }
}
