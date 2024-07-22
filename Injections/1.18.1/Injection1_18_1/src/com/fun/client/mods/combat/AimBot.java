package com.fun.client.mods.combat;

import com.fun.client.FunGhostClient;
import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.client.settings.Setting;
import com.fun.client.utils.Rotation.Rotation;
import com.fun.eventapi.event.events.EventMotion;
import com.fun.eventapi.event.events.EventUpdate;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.vecmath.Vector2f;

public class AimBot extends VModule {
    public AimBot() {
        super("AimBot", Category.Combat);
    }
    public Setting onAttack = new Setting("OnAttack", this, false);
    public Setting speed = new Setting("Speed", this, 50, 0, 100, false);

    @Override
    public void onMotion(EventMotion event) {
        super.onMotion(event);
        if(!onAttack.getValBoolean()||mc.mouseHandler.isLeftPressed()){
            Entity e=FunGhostClient.registerManager.vModuleManager.target.target;
            if(e==null)return;
            Vector2f v=aim(mc.player.getEyePosition(),e.getEyePosition());
            mc.player.setYRot(v.y);
            mc.player.setXRot(v.x);
        }
    }

    public static Vector2f aim(Vec3 player, Vec3 target) {
        double x = target.x - player.x;
        double z = target.z - player.z;
        double xx = x * x;
        double zz = z * z;
        double xz = Math.sqrt(xx + zz);

        return new Vector2f(-(float) (Math.atan2(target.y - player.y, xz) / (Math.PI / 180)),
                -(float) (Math.atan2(target.x - player.x, target.z - player.z) / (Math.PI / 180)));
    }

}
