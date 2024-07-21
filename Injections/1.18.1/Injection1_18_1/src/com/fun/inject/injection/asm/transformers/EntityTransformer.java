package com.fun.inject.injection.asm.transformers;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventStrafe;
import com.fun.inject.injection.asm.api.Inject;

import com.fun.inject.injection.asm.api.Transformer;

import com.fun.inject.Agent;
import com.fun.inject.Mappings;
import com.fun.inject.MinecraftVersion;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;

public class EntityTransformer extends Transformer {
    public EntityTransformer() {
        super(Classes.Entity);
    }
    @Inject(method = "moveRelative",descriptor = "(FLnet/minecraft/world/phys/Vec3;)V")
    public void onMoveFly(MethodNode methodNode) {
        InsnList list=new InsnList();//m_19920_
        AbstractInsnNode point = null;
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode aisn = methodNode.instructions.get(i);
            if (aisn instanceof MethodInsnNode meth) {

                if (meth.name.equals(Mappings.getObfMethod("m_20015_"))) {
                    point = aisn;
                }
            }
        }
        methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD,0));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC,Type.getInternalName(EntityTransformer.class),"onStrafe","(Ljava/lang/Object;Ljava/lang/Object;FF)Lcom/fun/inject/injection/asm/transformers/EntityTransformer$EventStructure;",false));
        int event= methodNode.maxLocals;
        list.add(new VarInsnNode(Opcodes.ASTORE,event));

        list.add(new VarInsnNode(Opcodes.ALOAD,event));
        list.add(new FieldInsnNode(Opcodes.GETFIELD,Type.getInternalName(EventStructure.class),"vec","L"+Mappings.getObfClass("net/minecraft/world/phys/Vec3")+";"));

        list.add(new VarInsnNode(Opcodes.ALOAD,event));
        list.add(new FieldInsnNode(Opcodes.GETFIELD,Type.getInternalName(EventStructure.class),"friction","F"));

        list.add(new VarInsnNode(Opcodes.ALOAD,event));
        list.add(new FieldInsnNode(Opcodes.GETFIELD,Type.getInternalName(EventStructure.class),"yaw","F"));
        methodNode.instructions.insertBefore(point,list);

    }
    public static EventStructure onStrafe(Object entity, Object moveVec, float friction, float yaw){
        //System.out.println("onStrafe1");
        //System.out.println("onStrafe");
        if(!(moveVec instanceof Vec3))throw new RuntimeException("invalid vec");
        System.out.println("strafe");
        EventStrafe eventStrafe=new EventStrafe((float) ((Vec3) moveVec).z, (float) ((Vec3) moveVec).x,yaw,friction);
        if(entity instanceof LocalPlayer) EventManager.call(eventStrafe);
        return new EventStructure(new Vec3(eventStrafe.strafe, ((Vec3) moveVec).y, eventStrafe.forward),
                eventStrafe.friction, eventStrafe.yaw);
    }
    public static class EventStructure{
        public Vec3 vec;
        public float friction,yaw;

        public EventStructure(Vec3 v,float friction,float yaw) {
            super();
            this.vec=v;
            this.yaw=yaw;
            this.friction=friction;
        }
    }
}
