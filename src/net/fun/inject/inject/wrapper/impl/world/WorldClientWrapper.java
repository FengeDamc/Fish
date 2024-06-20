package net.fun.inject.inject.wrapper.impl.world;


import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.Wrapper;
import net.fun.inject.inject.wrapper.impl.entity.EntityPlayerWrapper;
import net.fun.inject.inject.wrapper.impl.entity.EntityWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.viaversion.viaversion.libs.javassist.runtime.Desc.getClazz;

public class WorldClientWrapper extends Wrapper {
    public Object worldObj;

    public WorldClientWrapper() {
        super("net/minecraft/client/multiplayer/WorldClient");
    }

    public void setWorldObj(Object worldObj) {
        this.worldObj = worldObj;
    }

    public List<EntityWrapper> getLoadedEntities() {
        // FD: adm/f net/minecraft/world/World/field_72996_f

        try {

            Object value = ReflectionUtils.getFieldValue(worldObj,Mappings.getObfField("field_72996_f"));
            if (value instanceof List) {
                List<EntityWrapper> entities = new ArrayList<>();

                List<?> list = (List<?>) value;
                for (Object entityObj : list) {
                    entities.add(new EntityWrapper(entityObj));
                }
                return entities;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }
    public EntityWrapper getEntityByID(int id){
        //func_73045_a,getEntityByID,2,"Returns the Entity with the given ID, or null if it doesn't exist in this World."
        //MD: adm/a (I)Lpk; net/minecraft/world/World/func_73045_a (I)Lnet/minecraft/entity/Entity;
        return new EntityWrapper(ReflectionUtils.invokeMethod(worldObj,
                Mappings.getObfMethod("func_73045_a")
        ,new Class[]{int.class},id));
    }

    public List<EntityPlayerWrapper> getLoadedPlayers() {
        // FD: adm/j net/minecraft/world/World/field_73010_i

        try {

            Object value = ReflectionUtils.getFieldValue(worldObj,Mappings.getObfField("field_73010_i"));
            if (value instanceof List) {
                List<EntityPlayerWrapper> entities = new ArrayList<>();

                List list = (List) value;
                for (Object entityObj : list) {
                    entities.add(new EntityPlayerWrapper(entityObj));
                }

                return entities;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    public IBlockStateWrapper getBlockState(BlockPosWrapper wrapper) {
        return getBlockState(wrapper.get());
    }

    public IBlockStateWrapper getBlockState(Object blockPosObj) {
        try {
            Method method = getClazz().getMethod(Mappings.getObfMethod("func_180495_p"), BlockPosWrapper.getBlockPosClass());
            Object object = method.invoke(worldObj, blockPosObj);
            return new IBlockStateWrapper(object);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }

    public boolean isAir(Object blockPosObj) {
        // MD: adq/d (Lcj;)Z net/minecraft/world/IBlockAccess/func_175623_d (Lnet/minecraft/util/BlockPos;)Z

        try {
            Method method = getClazz().getMethod(Mappings.getObfMethod("func_175623_d"), BlockPosWrapper.getBlockPosClass());
            Object value = method.invoke(worldObj, blockPosObj);
            return value != null && (Boolean) value;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return true;
    }
}
