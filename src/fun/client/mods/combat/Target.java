package fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventStrafe;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.client.settings.Setting;
import fun.inject.Agent;
import fun.inject.inject.Mappings;
import fun.inject.inject.wrapper.impl.entity.EntityPlayerSPWrapper;
import fun.inject.inject.wrapper.impl.entity.EntityPlayerWrapper;
import fun.inject.inject.wrapper.impl.entity.EntityWrapper;
import fun.utils.Classes;

import java.util.ArrayList;

public class Target extends Module {
    public EntityWrapper target=null;
    public ArrayList<Object> bots=new ArrayList<>();
    public Setting onlyPlayer=new Setting("OnlyPlayer",this,false);
    public Setting antiBot=new Setting("AntiBot",this,false);
    public Setting range=new Setting("Range",this,6.0,0,6.0,false);
    public double dist=Double.MAX_VALUE;


    @Override
    public void onDisable() {
        super.onDisable();
        target=null;
        dist=Double.MAX_VALUE;

    }

    @Override
    public void onStrafe(EventStrafe event) {
        super.onStrafe(event);

        target=null;
        dist=Double.MAX_VALUE;
        EntityPlayerSPWrapper playersp = mc.getPlayer();

        //playersp.setPitch(0);


        //playersp.setYaw(0);
        //Agent.logger.info(mc.getWorld().worldObj);
        for (EntityWrapper player : onlyPlayer.getValBoolean()?
                mc.getWorld().getLoadedPlayers()
                :mc.getWorld().getLoadedEntities()) {
            double d1=mc.getPlayer().getDistance(player);
            if (player.entityObj!=playersp.getPlayerObj()&&d1< range.getValDouble()&&d1<dist&&!player.isDead()
                    &&Classes.EntityLivingBase.isInstanceof(player.entityObj)) {
                target = player;
                dist=d1;
                //Agent.logger.info(+":{}",);
            }
        }
        try {
            if (antiBot.getValBoolean()) {
                bots.clear();
                for (EntityPlayerWrapper p : mc.getWorld().getLoadedPlayers()) {
                    if (p.entityObj == null) {

                        continue;
                    }
                    if (Classes.EntityPlayerSP.isInstanceof(p.entityObj)) {
                        continue;
                    }
                    //"§"

                    if (p.getDisplayName().getUnformattedText().startsWith(mc.getPlayer().getDisplayName().getUnformattedText().substring(1, 3))) {
                        bots.add(p.entityObj);

                        continue;
                    }
                    if (mc.getNetHandler().getPlayerInfo(p.getUniqueID()) == null) {
                        bots.add(p.entityObj);


                        continue;
                    }
                    if (p.isInvisible()) {
                        bots.add(p.entityObj);



                        continue;
                    }
                    if (p.getTeam().obj != null && mc.getPlayer().getTeam().obj != null && p.getTeam().isSameTeam(mc.getPlayer().getTeam())) {
                        bots.add(p.entityObj);

                    }


                }
                if (target!=null&&bots.contains(target.entityObj)) target = null;

            }
        }
        catch (Exception e){
            e.printStackTrace();
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