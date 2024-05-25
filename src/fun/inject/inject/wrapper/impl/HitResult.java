package fun.inject.inject.wrapper.impl;

import fun.inject.inject.Mappings;
import fun.inject.inject.ReflectionUtils;
import fun.inject.inject.wrapper.impl.world.BlockPosWrapper;
import fun.inject.inject.wrapper.impl.world.BlockWrapper;
import fun.inject.inject.wrapper.impl.world.Vec3Wrapper;
import fun.utils.Fields;
import fun.utils.Vec3;

public class HitResult {

    public Type type;
    public Object mouseOverObj;

    public HitResult(Object objectMouseOverObj) {
        try {
            mouseOverObj=objectMouseOverObj;
            type = Type.valueOf(((Enum<?>)ReflectionUtils.getFieldValue(objectMouseOverObj, Fields.MovingObjType.getName())).name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Type getType() {
        return type == null ? Type.MISS : type;
    }
    public BlockPosWrapper getBlockPos(){

        return new BlockPosWrapper(ReflectionUtils.getFieldValue(mouseOverObj,Mappings.getObfField("field_178783_e")));
    }
    public Vec3Wrapper getHitVec(){

        return new Vec3Wrapper(ReflectionUtils.getFieldValue(mouseOverObj,Mappings.getObfField("field_72307_f")));
    }

    public enum Type {
        MISS, BLOCK, ENTITY
    }
}
