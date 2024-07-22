package com.fun.client.mods.combat;

import com.fun.client.settings.Setting;
import com.fun.client.utils.Rotation.Rotation;
import com.fun.eventapi.event.events.EventRender3D;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.inject.injection.wrapper.impl.entity.EntityPlayerSPWrapper;
import com.fun.inject.injection.wrapper.impl.entity.EntityWrapper;
import com.fun.utils.math.MathHelper;
import com.fun.utils.math.vecmath.Vec3;

import javax.vecmath.Vector2f;

import static com.fun.client.FunGhostClient.registerManager;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
public class AimAssist extends Module {
    public AimAssist() {
        super("AimAssist", Category.Combat);
    }

    public EntityWrapper target = null;
    public Rotation lastRotations = null;
    public Rotation rotations = null;

    public Setting onAttack = new Setting("OnAttack", this, false);
    public Setting onRotate = new Setting("OnRotate", this, false);
    public Setting speed = new Setting("Speed", this, 50, 0, 100, false);
    public Setting fov = new Setting("FOV", this, 90, 0, 180, true);

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);

        lastRotations = rotations;
        rotations = null;
        if (onAttack.getValBoolean() && !mc.getGameSettings().getKey("key.attack").isPressed()) return;

        target = registerManager.vModuleManager.target.target;
        EntityPlayerSPWrapper playersp = mc.getPlayer();

        if (target == null || playersp.isOpenGui()) return;

        Vector2f v = aim(new Vec3(playersp.getX(), playersp.getY() + playersp.getEyeHeight(), playersp.getZ()),
                new Vec3(target.getX(), target.getY() + target.getEyeHeight(), target.getZ()));

        this.rotations = new Rotation(v);
        // 检查目标是否在FOV范围内
        if (!isInFOV(rotations, playersp)) {
            rotations = null;
        }
    }

    @Override
    public void onRender3D(EventRender3D event) {
        super.onRender3D(event);
        EntityPlayerSPWrapper playersp = mc.getPlayer();

        if (rotations == null || lastRotations == null ||
                (mc.getMouseHelper().getDeltaY() == 0 && mc.getMouseHelper().getDeltaX() == 0 && onRotate.getValBoolean()))
            return;

        Vector2f rotations = new Vector2f(MathHelper.wrapAngleTo180_float(this.lastRotations.getYaw() +
                (this.rotations.getYaw() - this.lastRotations.getYaw()) * mc.getTimer().getRenderPartialTicks()), 0);
        final float strength = (float) speed.getValDouble();
        final float f = mc.getGameSettings().geMouseSensitivity() * 0.6F + 0.2F;
        final float gcd = f * f * f * 8.0F;

        int i = mc.getGameSettings().isInvertMouse() ? -1 : 1;
        float f2 = this.mc.getMouseHelper().getDeltaX() +
                (MathHelper.wrapAngleTo180_float(rotations.x - mc.getPlayer().getYaw()) * (strength / 100) -
                        this.mc.getMouseHelper().getDeltaX()) * gcd;
        float f3 = this.mc.getMouseHelper().getDeltaY() -
                this.mc.getMouseHelper().getDeltaY() * gcd;

        playersp.setAngles(f2, f3 * i);
    }

    public static float getAngleDifference(final float a, final float b) {
        return Float.parseFloat(Double.valueOf(MathHelper.wrapAngleTo180_double((a) - b)).toString());
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

    private boolean isInFOV(Rotation targetRotation, EntityPlayerSPWrapper player) {
        float yawDifference = getAngleDifference(player.getYaw(), targetRotation.getYaw());
        return Math.abs(yawDifference) <= fov.getValDouble() / 2;
    }
}