package com.fun.utils.version.fields;

import com.fun.inject.Agent;
import com.fun.inject.MinecraftType;
import com.fun.inject.MinecraftVersion;
import com.fun.inject.utils.ReflectionUtils;
import com.fun.utils.version.clazz.Classes;

import java.lang.reflect.Field;
import java.util.HashMap;

public enum Fields {
    sendQueueSP(new VField("field_71174_a")),
    player(new VField("field_71439_g"),
            new VField("field_71439_g").version(MinecraftVersion.VER_1122)),
    world(new VField("field_71439_g")),
    snake_MovementInput(new VField("field_78899_d")),
    MovingObjType(new VField("field_72313_a")),
    Yaw_Entity(Classes.Entity,new VField("field_70177_z")),
    Pitch_Entity(Classes.Entity,new VField("field_70125_A")),
    OnGround_Entity(Classes.Entity,new VField("field_70122_E")),
    MotionX(Classes.Entity,new VField("field_70159_w")),
    MotionY(Classes.Entity,new VField("field_70181_x")),
    MotionZ(Classes.Entity,new VField("field_70179_y")),
    posZ_Entity(Classes.Entity,new VField("field_70161_v")),
    posY_Entity(Classes.Entity,new VField("field_70163_u")),
    posX_Entity(Classes.Entity,new VField("field_70165_t")),
    pressTime_KeyBinding(Classes.KeyBinding,new VField("field_151474_i")),
    moveForward_MovementInput(Classes.MovementInput,new VField("field_78900_b"),
            new VField("field_192832_b").version(MinecraftVersion.VER_1122)),
    moveStrafe_MovementInput(Classes.MovementInput,new VField("field_78902_a"),
            new VField("field_78902_a").version(MinecraftVersion.VER_1122)),
    sideHit_MovingObj(new VField("field_178784_b")),
    mouseHelper(Classes.Minecraft,new VField("field_71417_B")),
    deltaX(Classes.MouseHelper,new VField("field_74377_a")),
    deltaY(Classes.MouseHelper,new VField("field_74375_b")),
    mouseSensitivity(new VField("field_74341_c")),
    invertMouse(new VField("field_74338_d")),
    renderPartialTicks(new VField("field_74281_c"),new VField("field_194147_b").version(MinecraftVersion.VER_1122)),
    entityId_S14PacketEntity(Classes.S14PacketEntity,new VField("field_149074_a")),
    channel_NetworkManager(new VField("field_150746_k")),
    packetListener_NetworkManager(new VField("field_150744_m")),
    POSITION_DefaultVertexFormats(Classes.DefaultVertexFormats,new VField("field_181705_e"));//POSITION field_181705_e
    //field_150744_m,packetListener

        public Field field;
        public String friendly_name;
        public String obf_name;
        public Classes parent;
        public HashMap<MinecraftType, HashMap<MinecraftVersion,VField>> map=new HashMap<>();

        /*Fields(String fname,Class<?> parent){
            try {
                //parent=Agent.findClass(Mappings.getObfClass(parentName));
                obf_name=Mappings.getObfField(fname);
                this.parent=parent;
                field= parent.getField(obf_name);
                friendly_name=fname;
            } catch (NoSuchFieldException e) {

            }
        }
        Fields(String fname){
            obf_name=Mappings.getObfField(fname);
            friendly_name=fname;
        }*/

    Fields(VField... VFields){
        for(MinecraftType type: MinecraftType.values()){
            map.put(type,new HashMap<>());
        }
        for(VField VField:VFields){
            map.get(VField.minecraftType).put(VField.minecraftVersion,VField);
        }
    }
    Fields(Classes parent,VField... VFields){
        this.parent=parent;
        for(MinecraftType type: MinecraftType.values()){
            map.put(type,new HashMap<>());
        }
        for(VField VField:VFields){
            map.get(VField.minecraftType).put(VField.minecraftVersion,VField);
        }
    }
    public Object get(Object target){
        if(target==null){
            return ReflectionUtils.getFieldValue(getParent(),this);
        }
        else return ReflectionUtils.getFieldValue(target,this);
    }
    public void set(Object target,Object value){
        if(target==null){
            ReflectionUtils.setFieldValue(getParent(),this,value);
        }
        else ReflectionUtils.setFieldValue(target,this,value);
    }
    public Classes getParent(){
        return parent;
    }

    public String getName(){
        VField V1=map.get(Agent.minecraftType).get(Agent.minecraftVersion);
        if(V1!=null)return V1.obf_name;
        VField V2=map.get(MinecraftType.VANILLA).get(Agent.minecraftVersion);
        if(V2!=null)return V2.obf_name;
        VField V3=map.get(MinecraftType.VANILLA).get(MinecraftVersion.VER_189);
        if(V3!=null)return V3.obf_name;
        Agent.logger.error("cant find fieldName");
        return null;
    }
    public VField getVFiled(){
        VField V1=map.get(Agent.minecraftType).get(Agent.minecraftVersion);
        if(V1!=null)return V1;
        VField V2=map.get(MinecraftType.VANILLA).get(Agent.minecraftVersion);
        if(V2!=null)return V2;
        VField V3=map.get(MinecraftType.VANILLA).get(MinecraftVersion.VER_189);
        if(V3!=null)return V3;
        Agent.logger.error("cant find VFiled");
        return null;
    }


}
