package com.fun.client.mods.combat;

import com.fun.eventapi.event.events.EventRender2D;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.utils.MathHelper;
import com.fun.utils.vecmath.Vec3;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.settings.Setting;
import com.fun.client.utils.Rotation.Rotation;
import com.fun.inject.inject.wrapper.impl.entity.EntityPlayerSPWrapper;
import com.fun.inject.inject.wrapper.impl.entity.EntityWrapper;
//import javax.vecmath.Vector2f;

import javax.vecmath.Vector2f;

import static com.fun.client.FunGhostClient.moduleManager;

public class AimAssist extends Module {
    public AimAssist() {
        super("AimAssist",Category.Combat);
    }
    public EntityWrapper target=null;
    public Rotation lastRotations =null;
    public Rotation rotations =null;

    public Setting onAttack=new Setting("OnAttack",this,false);
    public Setting onRotate=new Setting("OnRotate",this,false);
    //private final BoundsNumberValue strength = new BoundsNumberValue("AimAssist Strength", this, 30, 30, 1, 100, 1);
    //    private final BooleanValue onRotate = new BooleanValue("Only on mouse movement", this, true);


    public Setting speed=new Setting("Speed",this,50,0,180,false);

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);
        lastRotations = rotations;
        rotations = null;
        if(onAttack.getValBoolean()&&!mc.getGameSettings().getKey("key.attack").isPressed())return;
        target= moduleManager.target.target;
        EntityPlayerSPWrapper playersp=mc.getPlayer();

        if(target==null)return;
        Vector2f v= aim(new Vec3(playersp.getX(), playersp.getY()+ playersp.getEyeHeight(), playersp.getZ()),
                new Vec3(target.getX(),target.getY()+ target.getEyeHeight(),target.getZ()));

        this.rotations =new Rotation(v);//limitAngleChange(Rotation.player(),new Rotation(v), 1800);
        System.out.println("update rotations: "+rotations);
    }

    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        EntityPlayerSPWrapper playersp=mc.getPlayer();

        if(rotations==null||lastRotations==null||(mc.getMouseHelper().getDeltaY()==0&&mc.getMouseHelper().getDeltaX()==0&&onRotate.getValBoolean()))return;

        Vector2f rotations = new Vector2f(MathHelper.wrapAngleTo180_float(this.lastRotations.getYaw() + (this.rotations.getYaw() - this.lastRotations.getYaw()) * mc.getTimer().getRenderPartialTicks()), 0);
        final float strength = (float) speed.getValDouble();//(float) MathUtil.getRandom(this.strength.getValue().floatValue(), this.strength.getSecondValue().floatValue());

        final float f = mc.getGameSettings().geMouseSensitivity() * 0.6F + 0.2F;
        final float gcd = f * f * f * 8.0F;

        int i = mc.getGameSettings().isInvertMouse() ? -1 : 1;
        float f2 = this.mc.getMouseHelper().getDeltaX() + (MathHelper.wrapAngleTo180_float(rotations.x - mc.getPlayer().getYaw()) * (strength / 400) -
                this.mc.getMouseHelper().getDeltaX()) * gcd;
        float f3 = this.mc.getMouseHelper().getDeltaY() - this.mc.getMouseHelper().getDeltaY() * gcd;


        //EntityPlayerSPWrapper player= mc.getPlayer();

        playersp.setAngles(f2, f3 * (float) i);
        //System.out.printf("render2d %s %s%n",f2,f3*i);
    }

    public static float getAngleDifference(final float a, final float b) {
        return Float.parseFloat(Double.valueOf(MathHelper.wrapAngleTo180_double( (a)-b)).toString());//((((a - b) % 360.00F) + 540.00F) % 360.00F) - 180.00F;
    }
    public static Rotation limitAngleChange(final Rotation currentRotation, final Rotation targetRotation, final float turnSpeed) {
        float yawDifference = getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        float pitchDifference = getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());

        return new Rotation(
                currentRotation.getYaw() +((yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed))),
                currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed))
        );
    }

    public static Vector2f aim(Vec3 player, Vec3 target){

        double x = target.xCoord - player.xCoord;
        double z =target.zCoord - player.zCoord;
        double xx = x*x;
        double zz = z*z;
        double xz=Math.sqrt(xx+zz);

        return new Vector2f(-(float) ((float) ((float) Math.atan2(target.yCoord - player.yCoord,xz)/(Math.PI/180))),-(float) ((float) Math.atan2(target.xCoord- player.xCoord, target.zCoord -player.zCoord)/(Math.PI/180)));
        //Minecraft.getLogger().info((float) ((float) ((float) Math.atan2(entity.posY - mc.thePlayer.posY,xz)/(Math.PI/180)))+"   "+(float) ((float) Math.atan2(entity.posZ - mc.thePlayer.posZ, entity.posX - mc.thePlayer.posX)/(Math.PI/180)));
    }
}
