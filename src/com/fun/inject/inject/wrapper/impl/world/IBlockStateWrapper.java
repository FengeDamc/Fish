package com.fun.inject.inject.wrapper.impl.world;


import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.wrapper.Wrapper;

import java.lang.reflect.Method;

public class IBlockStateWrapper extends Wrapper {
    private final Object iBlockStateObj;

    private final BlockWrapper block = new BlockWrapper();

    public IBlockStateWrapper(Object iBlockStateObj) {
        super("net/minecraft/block/state/IBlockState");
        this.iBlockStateObj = iBlockStateObj;
    }

    public BlockWrapper getBlock() {
        try {
            Method method = getClazz().getMethod(Mappings.getObfMethod("func_177230_c"));
            Object value = method.invoke(iBlockStateObj);
            block.setBlockObj(value);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return block;
    }
}
