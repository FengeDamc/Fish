package net.fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventMoment;
import com.darkmagician6.eventapi.event.events.EventStrafe;
import com.darkmagician6.eventapi.event.events.EventTick;
import net.fun.client.FunGhostClient;
import net.fun.client.mods.Category;
import net.fun.client.mods.Module;
import net.fun.client.settings.Setting;
import net.fun.client.utils.Rotation.Rotation;
import net.fun.inject.Agent;
import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.wrapper.impl.entity.EntityPlayerSPWrapper;
import net.fun.inject.inject.wrapper.impl.entity.EntityWrapper;
import net.fun.utils.MathHelper;
import net.fun.utils.Vec3;
import org.lwjgl.util.vector.Vector2f;

import static net.fun.client.FunGhostClient.moduleManager;

public class AimBot extends Module {
    public AimBot() {
        super("AimBot",Category.Combat);
    }
    public EntityWrapper target=null;

    public Setting onAttack=new Setting("OnAttack",this,false);

    public Setting speed=new Setting("Speed",this,50,0,180,false);
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


    @Override
    public void onStrafe(EventStrafe event) {


        super.onStrafe(event);
        if(onAttack.getValBoolean()&&!mc.getGameSettings().getKey("key.attack").isPressed())return;
        target= moduleManager.target.target;
        EntityPlayerSPWrapper playersp=mc.getPlayer();


        if(target==null)return;


        //EntityPlayerSPWrapper player= mc.getPlayer();
        Vector2f v= aim(new Vec3(playersp.getX(), playersp.getY()+ playersp.getEyeHeight(), playersp.getZ()),
                new Vec3(target.getX(),target.getY()+ target.getEyeHeight(),target.getZ()));

        Rotation r=limitAngleChange(Rotation.player(),new Rotation(v), (float) speed.getValDouble());
        playersp.setPitch(r.getPitch());
        playersp.setYaw(r.getYaw());
        event.yaw=r.getYaw();
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
