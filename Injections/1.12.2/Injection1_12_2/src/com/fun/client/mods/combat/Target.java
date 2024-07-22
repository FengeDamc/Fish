package com.fun.client.mods.combat;

import com.fun.client.settings.Setting;
import com.fun.eventapi.event.events.EventStrafe;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.inject.Mappings;
import com.fun.inject.injection.wrapper.impl.entity.EntityPlayerSPWrapper;
import com.fun.inject.injection.wrapper.impl.entity.EntityPlayerWrapper;
import com.fun.inject.injection.wrapper.impl.entity.EntityWrapper;
import com.fun.utils.version.clazz.Classes;

import java.util.ArrayList;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
public class Target extends Module {
    public EntityWrapper target = null;
    public ArrayList<Object> bots = new ArrayList<>();
    public Setting onlyPlayer = new Setting("OnlyPlayer", this, false);
    public Setting antiBot = new Setting("AntiBot", this, false);
    public Setting range = new Setting("Range", this, 6.0, 0, 6.0, false);
    public Setting invisible = new Setting("Invisible", this, false);
    public double dist = Double.MAX_VALUE;

    @Override
    public void onDisable() {
        super.onDisable();
        target = null;
        dist = Double.MAX_VALUE;
    }

    @Override
    public void onStrafe(EventStrafe event) {}

    @Override
    public void onUpdate(EventUpdate event) {
        target = null;
        dist = Double.MAX_VALUE;
        EntityPlayerSPWrapper playersp = mc.getPlayer();

        for (EntityWrapper player : onlyPlayer.getValBoolean() ? mc.getWorld().getLoadedPlayers() : mc.getWorld().getLoadedEntities()) {
            double d1 = mc.getPlayer().getDistance(player);
            if (player.obj != playersp.getEntityObj() && d1 < range.getValDouble() && d1 < dist && !player.isDead()
                    && Classes.EntityLivingBase.isInstanceof(player.obj) && (invisible.getValBoolean() || !player.isInvisible())) {
                target = player;
                dist = d1;
            }
        }

        try {
            if (antiBot.getValBoolean()) {
                bots.clear();
                for (EntityPlayerWrapper p : mc.getWorld().getLoadedPlayers()) {
                    if (p.obj == null) continue;
                    if (Classes.EntityPlayerSP.isInstanceof(p.obj)) continue;

                    if (p.getDisplayName().getUnformattedText().startsWith(mc.getPlayer().getDisplayName().getUnformattedText().substring(1, 3))) {
                        bots.add(p.obj);
                        continue;
                    }
                    if (mc.getNetHandler().getPlayerInfo(p.getUniqueID()) == null) {
                        bots.add(p.obj);
                        continue;
                    }
                    if (p.isInvisible() && !invisible.getValBoolean()) {
                        bots.add(p.obj);
                        continue;
                    }
                    if (p.getTeam().obj != null && mc.getPlayer().getTeam().obj != null && p.getTeam().isSameTeam(mc.getPlayer().getTeam())) {
                        bots.add(p.obj);
                    }
                }
                if (target != null && bots.contains(target.obj)) target = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Target() {
        super("Target", Category.Combat);
    }

    public boolean isEntityLivingBase(Object instance) {
        Class<?> c = instance.getClass();
        while (c.getSuperclass() != null) {
            if (c.getName().equals(Mappings.getObfClass("net/minecraft/entity/EntityLivingBase").replace('/', '.')))
                return true;
            c = c.getSuperclass();
        }
        return false;
    }
}
