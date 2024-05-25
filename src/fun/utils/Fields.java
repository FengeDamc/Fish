package fun.utils;

import fun.inject.Agent;
import fun.inject.inject.Mappings;
import fun.inject.inject.MinecraftType;
import fun.inject.inject.MinecraftVersion;

import java.lang.reflect.Field;
import java.util.HashMap;

public enum Fields {
    sendQueueSP(new VField("field_71174_a")),
    player(new VField("field_71439_g"),
            new VField("field_71439_g").version(MinecraftVersion.VER_1122)),
    world(new VField("field_71439_g")),
    snake_MovementInput(new VField("field_78899_d")),
    MovingObjType(new VField("field_72313_a")),
    Yaw_Entity(new VField("field_70177_z")),
    Pitch_Entity(new VField("field_70125_A")),
    OnGround_Entity(new VField("field_70122_E")),
    MotionX(new VField("field_70159_w")),
    MotionY(new VField("field_70181_x")),
    MotionZ(new VField("field_70179_y")),
    posZ_Entity(new VField("field_70161_v")),
    posY_Entity(new VField("field_70163_u")),
    posX_Entity(new VField("field_70165_t"));

        public Field field;
        public String friendly_name;
        public String obf_name;
        public Class<?> parent;
        public HashMap<MinecraftType, HashMap<MinecraftVersion,VField>> map=new HashMap<>();

        Fields(String fname,Class<?> parent){
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
        }

    Fields(VField... VFields){
        for(MinecraftType type: MinecraftType.values()){
            map.put(type,new HashMap<>());
        }
        for(VField VField:VFields){
            map.get(VField.minecraftType).put(VField.minecraftVersion,VField);
        }
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


}
