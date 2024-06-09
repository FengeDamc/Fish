package fun.client.mods.combat;

import com.darkmagician6.eventapi.event.events.EventTick;
import com.darkmagician6.eventapi.event.events.EventUpdate;
import fun.client.FunGhostClient;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.client.settings.Setting;
import fun.inject.inject.wrapper.impl.entity.EntityPlayerSPWrapper;
import fun.inject.inject.wrapper.impl.entity.EntityWrapper;
import fun.inject.inject.wrapper.impl.setting.GameSettingsWrapper;
import fun.utils.Vec3;
import org.lwjgl.util.vector.Vector2f;

import static fun.client.mods.combat.AimBot.aim;

public class AutoBlocking extends Module {
    public AutoBlocking() {
        super("AutoBlocking",Category.Combat);

    }
    public boolean block=false;

    public Setting blockHurtTime=new Setting("BlockHurtTime",this,5,0,10,true);
    public Setting maxBlockRange =new Setting("MaxBlockRange",this,2,0,6,false)
    {
        @Override
        public void setValDouble(double in) {
            if(in<minBlockRange.getValDouble())in=minBlockRange.getValDouble();
            super.setValDouble(in);
        }
    };


    public Setting minBlockRange =new Setting("MinBlockRange",this,2,0,6,false){
        @Override
        public void setValDouble(double in) {
            if(in>maxBlockRange.getValDouble())in=maxBlockRange.getValDouble();
            super.setValDouble(in);
        }
    };

    public Setting fov=new Setting("FOV",this,80,0,180,true);

    @Override
    public void onTick(EventTick event) {
        super.onTick(event);
        mc.getGameSettings().getKey(GameSettingsWrapper.USE).setPressed(block|mc.getGameSettings().getKey(GameSettingsWrapper.USE).isPressed());


    }

    @Override
    public void onUpdate(EventUpdate event) {
        super.onUpdate(event);

        EntityWrapper target= FunGhostClient.moduleManager.target.target;
        if(target==null||FunGhostClient.moduleManager.target.dist< minBlockRange.getValDouble()||FunGhostClient.moduleManager.target.dist>maxBlockRange.getValDouble()) {
            block=false;
            return;
        }
        EntityPlayerSPWrapper playersp=mc.getPlayer();
        Vector2f v= aim(new Vec3(target.getX(),target.getY()+ target.getEyeHeight(),target.getZ()),
                new Vec3(playersp.getX(), playersp.getY()+ playersp.getEyeHeight(), playersp.getZ()));

        if(Math.abs(v.y-target.getYaw())<fov.getValDouble()) block=mc.getPlayer().getHurtTime()< blockHurtTime.getValDouble();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        block=false;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        block=false;

    }
}
