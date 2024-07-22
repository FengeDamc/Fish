package com.fun.client.mods.combat;

import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.utils.math.vecmath.Vec3;

import javax.vecmath.Vector2f;

public class AimBot extends Module {
    public AimBot() {
        super("AimBot", Category.Combat);
    }
    public static Vector2f aim(Vec3 player, Vec3 target) {
        double x = target.xCoord - player.xCoord;
        double z = target.zCoord - player.zCoord;
        double xx = x * x;
        double zz = z * z;
        double xz = Math.sqrt(xx + zz);

        return new Vector2f(-(float) (Math.atan2(target.yCoord - player.yCoord, xz) / (Math.PI / 180)),
                -(float) (Math.atan2(target.xCoord - player.xCoord, target.zCoord - player.zCoord) / (Math.PI / 180)));
    }

}
