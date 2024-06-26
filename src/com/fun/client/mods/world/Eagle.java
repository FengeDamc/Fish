package com.fun.client.mods.world;

import com.fun.eventapi.event.events.EventMoment;
import com.fun.utils.vecmath.Vec3;
import com.fun.client.mods.Category;
import com.fun.client.mods.Module;
import com.fun.client.mods.combat.AimBot;
import com.fun.inject.inject.wrapper.impl.HitResult;
import com.fun.inject.inject.wrapper.impl.world.BlockPosWrapper;

public class Eagle extends Module {
    private int dis=0;
    public Eagle() {
        super("Eagle",Category.World);
    }
    public float getMotionYaw(){
        float y1= AimBot.aim(new Vec3(0,0,0),new Vec3(mc.getPlayer().getMotionX(),mc.getPlayer().getMotionY(),mc.getPlayer().getMotionZ())).y;
        int time= Math.round(y1/45.0f);
        y1=time*45;

        return y1;
    }



    @Override
    public void onMoment(EventMoment event) {
        super.onMoment(event);
        HitResult hitResult=mc.getHitResult();
        BlockPosWrapper blockPos=hitResult.getBlockPos();
        if(blockPos==null||hitResult.face==null)return;
        //Agent.System.out.println("eagle");
        //Agent.System.out.println("target x:{} y:{} z:{}",blockPos.x,blockPos.y,blockPos.z);
        BlockPosWrapper playerDownPos= mc.getPlayer().getPos().add(0,-1,0);
        //Agent.System.out.println("player x:{} y:{} z:{}",playerDownPos.x,playerDownPos.y,playerDownPos.z);
        //Agent.System.out.println(playerDownPos.get().equals(blockPos.get()));

        //int xDiff=playerDownPos.x-blockPos.x;
        //int yDiff=playerDownPos.y- blockPos.y;
        //int zDiff=playerDownPos.z- blockPos.z;
        //System.out.println(hitResult.face.offset);
        //Agent.System.out.println("{}",blockPos.add(hitResult.face.offset).equals(playerDownPos));
        //BlockPosWrapper targetPos=blockPos.add(hitResult.face.offset);
        Vec3 target=blockPos.toVec3();
        Vec3 player=mc.getPlayer().getPosVec().addVector(0,-0.5,0);
        //Agent.System.out.println("{} {} {}",posIn(target,player,0.5),target,player);
        if(posIn(target,player,1)){
            if(mc.getWorld().isAir(playerDownPos.get())){
                if(!mc.getPlayer().getMovementInputObj().getSneak()){
                    mc.getPlayer().getMovementInputObj().setMoveForward(mc.getPlayer().getMovementInputObj().getMoveForward()*0.3f);
                    mc.getPlayer().getMovementInputObj().setMoveStrafe(mc.getPlayer().getMovementInputObj().getMoveStrafe()*0.3f);
                }
                mc.getPlayer().getMovementInputObj().setSneak(true);


            }


        }


    }
    public boolean posIn(Vec3 c,Vec3 t,double r){
        return t.xCoord<=c.xCoord+r&&t.xCoord>=c.xCoord-r&&
                t.yCoord<=c.yCoord+r&&t.yCoord>=c.yCoord-r&&
                t.zCoord<=c.zCoord+r&&t.zCoord>=c.zCoord-r;
    }
}
