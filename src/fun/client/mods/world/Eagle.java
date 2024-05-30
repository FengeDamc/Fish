package fun.client.mods.world;

import com.darkmagician6.eventapi.event.events.EventMoment;
import com.darkmagician6.eventapi.event.events.EventStrafe;
import com.darkmagician6.eventapi.event.events.EventUpdate;
import fun.client.mods.Category;
import fun.client.mods.Module;
import fun.inject.Agent;
import fun.inject.inject.wrapper.impl.HitResult;
import fun.inject.inject.wrapper.impl.world.BlockPosWrapper;
import fun.inject.inject.wrapper.impl.world.Vec3Wrapper;
import fun.utils.Vec3;

public class Eagle extends Module {
    private int dis=0;
    public Eagle() {
        super("Eagle",Category.World);
    }


    @Override
    public void onMoment(EventMoment event) {
        super.onMoment(event);
        HitResult hitResult=mc.getHitResult();
        BlockPosWrapper blockPos=hitResult.getBlockPos();
        //Agent.logger.info("eagle");
        //Agent.logger.info("target x:{} y:{} z:{}",blockPos.x,blockPos.y,blockPos.z);
        BlockPosWrapper playerDownPos= mc.getPlayer().getPos().add(0,-1,0);
        //Agent.logger.info("player x:{} y:{} z:{}",playerDownPos.x,playerDownPos.y,playerDownPos.z);
        //Agent.logger.info(playerDownPos.get().equals(blockPos.get()));


        if(Math.abs(playerDownPos.x-blockPos.x)+Math.abs(playerDownPos.z- blockPos.z)+Math.abs(playerDownPos.y- blockPos.y)<=2){
            if(mc.getWorld().isAir(playerDownPos.get())){
                mc.getPlayer().getMovementInputObj().setSneak(true);


            }


        }


    }
}
