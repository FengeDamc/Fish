package fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventMotion;
import com.darkmagician6.eventapi.event.events.EventRender3D;
import com.darkmagician6.eventapi.event.events.EventStrafe;
import com.darkmagician6.eventapi.event.events.EventUpdate;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.client.settings.Setting;
import fun.client.utils.Rotation.Rotation;
import fun.inject.Agent;
import fun.inject.inject.Mappings;
import fun.inject.inject.wrapper.impl.entity.EntityPlayerSPWrapper;
import fun.inject.inject.wrapper.impl.entity.EntityWrapper;
import fun.utils.MathHelper;
import fun.utils.Vec3;
import org.lwjgl.util.vector.Vector2f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class AimBot extends Module {
    public AimBot() {
        super("AimBot",Category.Combat);
    }
    public EntityWrapper target=null;

    public Setting onlyPlayer=new Setting("OnlyPlayer",this,false);
    public Setting onAttack=new Setting("OnAttack",this,false);

    public Setting range=new Setting("Range",this,6.0,0,6.0,false);
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
    public void onRender3D(EventRender3D event) {
        super.onRender3D(event);
        if(onAttack.getValBoolean()&&!mc.getGameSettings().getKey("key.attack").isPressed())return;
        target=null;
        EntityPlayerSPWrapper playersp = mc.getPlayer();

        //playersp.setPitch(0);


        //playersp.setYaw(0);
        //Agent.logger.info(mc.getWorld().worldObj);
        for (EntityWrapper player : onlyPlayer.getValBoolean()?
                mc.getWorld().getLoadedPlayers()
                :mc.getWorld().getLoadedEntities()) {
            if (player.entityObj!=playersp.getPlayerObj()&&mc.getPlayer().getDistance(player) < range.getValDouble()&&!player.isDead()
                    &&isEntityLivingBase(player.entityObj)) {
                target = player;
                //Agent.logger.info(+":{}",);
            }
        }


        if(target==null)return;


        //EntityPlayerSPWrapper player= mc.getPlayer();
        Vector2f v= aim(new Vec3(playersp.getX(), playersp.getY()+ playersp.getEyeHeight(), playersp.getZ()),
                new Vec3(target.getX(),target.getY()+ target.getEyeHeight(),target.getZ()));

        Rotation r=limitAngleChange(Rotation.player(),new Rotation(v), (float) speed.getValDouble());
        playersp.setPitch(r.getPitch());
        playersp.setYaw(r.getYaw());


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
