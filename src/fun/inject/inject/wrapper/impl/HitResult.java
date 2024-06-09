package fun.inject.inject.wrapper.impl;

import fun.inject.inject.Mappings;
import fun.inject.inject.ReflectionUtils;
import fun.inject.inject.wrapper.impl.world.BlockPosWrapper;
import fun.inject.inject.wrapper.impl.world.BlockWrapper;
import fun.inject.inject.wrapper.impl.world.Vec3Wrapper;
import fun.utils.Fields;
import fun.utils.Vec3;
import fun.utils.Vec3i;

public class HitResult {

    public Type type;
    public Facing face;
    public Object mouseOverObj;

    public HitResult(Object objectMouseOverObj) {
        try {
            mouseOverObj=objectMouseOverObj;
            type = Type.valueOf(((Enum<?>)ReflectionUtils.getFieldValue(objectMouseOverObj, Fields.MovingObjType.getName())).name());
            Enum e1=((Enum<?>)Fields.sideHit_MovingObj.get(objectMouseOverObj));
            face = e1==null?null:Facing.valueOf(e1.name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Type getType() {
        return type == null ? Type.MISS : type;
    }
    public BlockPosWrapper getBlockPos(){
        BlockPosWrapper b=new BlockPosWrapper(ReflectionUtils.getFieldValue(mouseOverObj,Mappings.getObfField("field_178783_e")));
        return b.blockPosObj==null?null:b;
    }
    public Vec3Wrapper getHitVec(){

        return new Vec3Wrapper(ReflectionUtils.getFieldValue(mouseOverObj,Mappings.getObfField("field_72307_f")));
    }

    public enum Type {
        MISS, BLOCK, ENTITY
    }
    public enum Facing{
        DOWN(new Vec3i(0, -1, 0))
        ,UP(new Vec3i(0, 1, 0)),
        NORTH(new Vec3i(0, 0, -1))
        ,SOUTH(new Vec3i(0, 0, 1))
        ,WEST(new Vec3i(-1, 0, 0))
        ,EAST (new Vec3i(1, 0, 0));
        public final Vec3i offset;
        Facing(Vec3i offsetIn){
            this.offset=offsetIn;
        }
    }
}
