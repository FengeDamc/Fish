package com.fun.client.mods.combat;

import com.fun.eventapi.event.events.EventStrafe;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.utils.version.clazz.Classes;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.wrapper.impl.entity.EntityPlayerSPWrapper;
import com.fun.inject.inject.wrapper.impl.entity.EntityPlayerWrapper;
import com.fun.inject.inject.wrapper.impl.entity.EntityWrapper;

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

    }

    @Override
    public void onUpdate(EventUpdate event) {

        target=null;
        dist=Double.MAX_VALUE;
        EntityPlayerSPWrapper playersp = mc.getPlayer();

        //playersp.setPitch(0);


        //playersp.setYaw(0);
        //Agent.System.out.println(mc.getWorld().worldObj);
        for (EntityWrapper player : onlyPlayer.getValBoolean()?
                mc.getWorld().getLoadedPlayers()
                :mc.getWorld().getLoadedEntities()) {
            double d1=mc.getPlayer().getDistance(player);
            if (player.obj !=playersp.getEntityObj()&&d1< range.getValDouble()&&d1<dist&&!player.isDead()
                    && Classes.EntityLivingBase.isInstanceof(player.obj)) {
                target = player;
                dist=d1;
                //Agent.System.out.println(+":{}",);
            }
        }
        try {
            if (antiBot.getValBoolean()) {
                bots.clear();
                for (EntityPlayerWrapper p : mc.getWorld().getLoadedPlayers()) {
                    if (p.obj == null) {

                        continue;
                    }
                    if (Classes.EntityPlayerSP.isInstanceof(p.obj)) {
                        continue;
                    }
                    //"§"

                    if (p.getDisplayName().getUnformattedText().startsWith(mc.getPlayer().getDisplayName().getUnformattedText().substring(1, 3))) {
                        bots.add(p.obj);

                        continue;
                    }
                    if (mc.getNetHandler().getPlayerInfo(p.getUniqueID()) == null) {
                        bots.add(p.obj);


                        continue;
                    }
                    if (p.isInvisible()) {
                        bots.add(p.obj);



                        continue;
                    }
                    if (p.getTeam().obj != null && mc.getPlayer().getTeam().obj != null && p.getTeam().isSameTeam(mc.getPlayer().getTeam())) {
                        bots.add(p.obj);

                    }


                }
                if (target!=null&&bots.contains(target.obj)) target = null;

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
            //Agent.System.out.println(c.getName());
            if(c.getName().equals(Mappings.getObfClass("net/minecraft/entity/EntityLivingBase").replace('/','.')))
                return true;
            c = c.getSuperclass();
        }
        return false;
    }
}
