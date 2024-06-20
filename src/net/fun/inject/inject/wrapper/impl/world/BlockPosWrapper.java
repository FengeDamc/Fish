package net.fun.inject.inject.wrapper.impl.world;


import net.fun.inject.Agent;
import net.fun.inject.inject.Mappings;
import net.fun.inject.inject.ReflectionUtils;
import net.fun.inject.inject.wrapper.Wrapper;
import net.fun.utils.Classes;
import net.fun.utils.Vec3;
import net.fun.utils.Vec3i;
import jdk.nashorn.internal.ir.Block;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BlockPosWrapper extends Wrapper {
    private static Class<?> blockPosClass;
    private static final Map<Integer, Object> blockPosCache = new HashMap<>();

    public Object blockPosObj;
    public int x, y, z;


    public BlockPosWrapper(Object blockPosObj) {
        super(Classes.BlockPos);
        if(blockPosObj==null){
            return;
        }
        this.blockPosObj = blockPosObj;

        //FD: df/a net/minecraft/util/Vec3i/field_177962_a x
        //FD: df/c net/minecraft/util/Vec3i/field_177960_b y
        //FD: df/d net/minecraft/util/Vec3i/field_177961_c z
        //MD: df/n ()I net/minecraft/util/Vec3i/func_177958_n ()I getX
        //MD: df/o ()I net/minecraft/util/Vec3i/func_177956_o ()I getY
        //MD: df/p ()I net/minecraft/util/Vec3i/func_177952_p ()I getZ

        try {

            x = (int) ReflectionUtils.invokeMethod(this.blockPosObj,Mappings.getObfMethod("func_177958_n"));



            y = (int) ReflectionUtils.invokeMethod(this.blockPosObj,Mappings.getObfMethod("func_177956_o"));



            z = (int) ReflectionUtils.invokeMethod(this.blockPosObj,Mappings.getObfMethod("func_177952_p"));





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Vec3 toVec3(){
        return new Vec3(x+0.5,y,z+0.5);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BlockPosWrapper){
            return this.x==((BlockPosWrapper) obj).x&&
                    this.y==((BlockPosWrapper) obj).y&&
                    this.z==((BlockPosWrapper) obj).z;
        }
        return super.equals(obj);
    }

    public BlockPosWrapper(int x, int y, int z) {
        super(Classes.BlockPos);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPosWrapper(double x, double y, double z) {
        super(Classes.BlockPos);
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    public BlockPosWrapper add(int x, int y, int z) {
        return new BlockPosWrapper(this.x + x, this.y + y, this.z + z);
    }

    public Object get() {
        return create(x, y, z);
    }

    public static Object create(int x, int y, int z) {
        return ReflectionUtils.newInstance(getBlockPosClass(),new Class[]{int.class,int.class,int.class},x,y,z);
    }
    public static Object create(double x, double y, double z) {
        return ReflectionUtils.newInstance(getBlockPosClass(),new Class[]{double.class,double.class,double.class},x,y,z);//getBlockPosClass().getConstructors()[4].newInstance(x, y, z);

    }


    public static Object createIntoCache(int x, int y, int z) {
        int hash = hash(x, y, z);
        if (!blockPosCache.containsKey(hash)) {
            try {
                Object blockPosObj = ReflectionUtils.newInstance(getBlockPosClass(),new Class[]{int.class,int.class,int.class},x,y,z);//getBlockPosClass().getConstructors()[4].newInstance(x, y, z);
                blockPosCache.put(hash, blockPosObj);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return blockPosCache.get(hash);
    }

    public static Object createIntoCache(double x, double y, double z) {
        int hash = hash((int) x, (int) y, (int) z);
        if (!blockPosCache.containsKey(hash)) {
            try {
                Object blockPosObj = ReflectionUtils.newInstance(getBlockPosClass(),new Class[]{int.class,int.class,int.class},(int)x,(int)y,(int)z);//getBlockPosClass().getConstructors()[4].newInstance(x, y, z);
                blockPosCache.put(hash, blockPosObj);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        return blockPosCache.get(hash);
    }

    public static Class<?> getBlockPosClass() {
        if (blockPosClass == null) {
            try {
                blockPosClass = Classes.BlockPos.getClazz();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return blockPosClass;
    }

    public static int hash(int x, int y, int z) {
        return (y + z * 31) * 31 + x;
    }

    public BlockPosWrapper add(Vec3i offset) {
        return add(offset.getX(),offset.getY(), offset.getZ());
    }
}
