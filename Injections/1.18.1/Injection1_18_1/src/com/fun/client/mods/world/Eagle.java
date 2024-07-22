package com.fun.client.mods.world;

import com.fun.client.mods.Category;
import com.fun.client.mods.VModule;
import com.fun.eventapi.event.events.EventMoment;

import com.fun.inject.injection.wrapper.impl.world.BlockPosWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Eagle extends VModule {
    public Eagle() {
        super("Eagle",Category.World);
    }

    @Override
    public void onMoment(EventMoment event) {
        super.onMoment(event);
        HitResult hr=mc.hitResult;
        if(hr instanceof BlockHitResult &&hr.getType()== HitResult.Type.BLOCK){
            BlockPos blockPos=((BlockHitResult) hr).getBlockPos();
            BlockPos playerDownPos=getPos(mc.player.getPosition(1)).offset(0,-1,0);
            Vec3 target=toVec3(blockPos);
            Direction face=((BlockHitResult) hr).getDirection();
            Vec3 player=mc.player.getPosition(1).add(0,-0.5,0);
            if(posIn(target,player,1)){
                if(mc.level.getBlockState(playerDownPos).getMaterial()== Material.AIR){
                    if(!mc.player.input.shiftKeyDown){
                        mc.player.input.forwardImpulse*=0.3f;
                        mc.player.input.leftImpulse*=0.3f;
                    }
                    mc.player.input.shiftKeyDown=true;



                }


            }

        }
    }
    public Vec3 toVec3(int x,int y,int z) {
        return new Vec3(x + 0.5, y, z + 0.5);
    }
    public Vec3 toVec3(BlockPos pos){
        return toVec3(pos.getX(),pos.getY(),pos.getZ());
    }
    public boolean posIn(Vec3 c, Vec3 t, double r){
        return t.x<=c.x+r&&t.x>=c.x-r&&
                t.y<=c.y+r&&t.y>=c.y-r&&
                t.z<=c.z+r&&t.z>=c.z-r;
    }
    public BlockPos getPos(Vec3 v) {
        return new BlockPos(Math.floor(v.x), Math.floor(v.y), Math.floor(v.z));
    }
}
