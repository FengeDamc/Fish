package com.fun.inject.injection.wrapper.impl.world;


import com.fun.inject.Mappings;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.inject.injection.wrapper.Wrapper;
import com.fun.inject.injection.wrapper.impl.entity.EntityPlayerWrapper;
import com.fun.inject.injection.wrapper.impl.entity.EntityWrapper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class WorldClientWrapper extends Wrapper {
    public ClientLevel worldObj;

    public WorldClientWrapper() {
        super("net/minecraft/client/multiplayer/WorldClient");
    }

    public void setWorldObj(ClientLevel worldObj) {
        this.worldObj = worldObj;
    }

    public List<EntityWrapper> getLoadedEntities() {
        // FD: adm/f net/minecraft/world/World/field_72996_f
        ArrayList<EntityWrapper> es=new ArrayList<>();
        for(Entity e:worldObj.entitiesForRendering()){
            es.add(new EntityWrapper(e));
        }
        return es;
    }
    public EntityWrapper getEntityByID(int id){
        //func_73045_a,getEntityByID,2,"Returns the Entity with the given ID, or null if it doesn't exist in this World."
        //MD: adm/a (I)Lpk; net/minecraft/world/World/func_73045_a (I)Lnet/minecraft/world/entity/Entity;
        return new EntityWrapper(worldObj.getEntity(id));
    }

    public List<EntityPlayerWrapper> getLoadedPlayers() {
        // FD: adm/j net/minecraft/world/World/field_73010_i
        ArrayList<EntityPlayerWrapper> es=new ArrayList<>();
        for(Player e:worldObj.players()){
            es.add(new EntityPlayerWrapper(e));
        }
        return es;
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
