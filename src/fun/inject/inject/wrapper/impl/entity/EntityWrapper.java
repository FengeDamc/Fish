package fun.inject.inject.wrapper.impl.entity;


import fun.inject.inject.Mappings;
import fun.inject.inject.ReflectionUtils;
import fun.inject.inject.wrapper.Wrapper;
import fun.inject.inject.wrapper.impl.world.BlockPosWrapper;

public class EntityWrapper extends Wrapper {
    public Object entityObj;

    public EntityWrapper(Object entityObj) {
        super("net/minecraft/entity/Entity");
        this.entityObj = entityObj;
    }
    public EntityWrapper(Object entityObj,String clazz) {
        super(clazz);
        this.entityObj = entityObj;
    }
    public float getEyeHeight(){
        //func_70047_e getEyeHeight
        return (float) ReflectionUtils.invokeMethod(entityObj,Mappings.getObfMethod("func_70047_e"));

    }
    public boolean isDead(){
        return (boolean) ReflectionUtils.getFieldValue(entityObj,Mappings.getObfField("field_70128_L"));

    }
    public float getHealth(){
        return (float) ReflectionUtils.invokeMethod(entityObj,Mappings.getObfMethod("func_110143_aJ"));//func_110143_aJ
    }


    public double getX() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70165_t");
        Object value = ReflectionUtils.getFieldValue(entityObj, notch);
        return value == null ? 0.0 : (Double) value;
    }

    public double getY() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70163_u");
        Object value = ReflectionUtils.getFieldValue(entityObj, notch);
        return value == null ? 0.0 : (Double) value;
    }
    public int getEntityID() {
        return (int) ReflectionUtils.invokeMethod(entityObj, Mappings.getObfMethod("func_145782_y"));
    }

    public double getZ() {
        // FD: pk/s net/minecraft/entity/Entity/field_70165_t

        String notch = Mappings.getObfField("field_70161_v");
        Object value = ReflectionUtils.getFieldValue(entityObj, notch);
        return value == null ? 0.0 : (Double) value;
    }

    public BlockPosWrapper getPos() {
        return new BlockPosWrapper(Math.floor(getX()), Math.floor(getY()), Math.floor(getZ()));
    }

    public Object getPosObj() {
        return BlockPosWrapper.create(getX(), getY(), getZ());
    }
}
