package com.fun.inject.inject.asm.transformers;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventStrafe;
import com.fun.inject.inject.asm.api.Mixin;
import com.fun.inject.inject.asm.api.Transformer;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.inject.Agent;
import com.fun.inject.inject.Mappings;
import com.fun.inject.inject.MinecraftVersion;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;

public class EntityTransformer extends Transformer {
    public EntityTransformer() {
        super(Classes.Entity);
    }
    @Mixin(method = Methods.moveFlying_Entity)
    public void onMoveFly(MethodNode methodNode) {
        final int ASTRAFE = Agent.minecraftVersion== MinecraftVersion.VER_189||
                Agent.minecraftVersion== MinecraftVersion.VER_1710?0:1;
        InsnList list = new InsnList();
        //Agent.System.out.println("moveFlying");
        int j =0;
        list.add(new VarInsnNode(Opcodes.ALOAD,0));

        list.add(new VarInsnNode(Opcodes.FLOAD,1));
        list.add(new VarInsnNode(Opcodes.FLOAD,2+ASTRAFE));
        list.add(new VarInsnNode(Opcodes.FLOAD,3+ASTRAFE));

        list.add(new VarInsnNode(Opcodes.ALOAD,0));
        //FD: pk/s net/minecraft/entity/Entity/field_70165_t
        //FD: pk/t net/minecraft/entity/Entity/field_70163_u
        //FD: pk/u net/minecraft/entity/Entity/field_70161_v
        //FD: pk/y net/minecraft/entity/Entity/field_70177_z
        //FD: pk/z net/minecraft/entity/Entity/field_70125_A
        list.add(new FieldInsnNode(Opcodes.GETFIELD,Mappings.getObfClass("net/minecraft/entity/Entity"), Mappings.getObfField("field_70177_z"),"F"));
        //event参数

        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EntityTransformer.class),"onStrafe","(Ljava/lang/Object;FFFF)Lcom/fun/eventapi/event/events/EventStrafe;"));
        list.add(new VarInsnNode(Opcodes.ASTORE,4+ASTRAFE));
        j++;
        list.add(new VarInsnNode(Opcodes.ALOAD,4+ASTRAFE));
        list.add(new FieldInsnNode(Opcodes.GETFIELD,"com/fun/eventapi/event/events/EventStrafe","strafe","F"));

        list.add(new VarInsnNode(Opcodes.FSTORE,1));

        list.add(new VarInsnNode(Opcodes.ALOAD,4+ASTRAFE));
        list.add(new FieldInsnNode(Opcodes.GETFIELD,"com/fun/eventapi/event/events/EventStrafe","forward","F"));

        list.add(new VarInsnNode(Opcodes.FSTORE,2+ASTRAFE));

        list.add(new VarInsnNode(Opcodes.ALOAD,4+ASTRAFE));
        list.add(new FieldInsnNode(Opcodes.GETFIELD,"com/fun/eventapi/event/events/EventStrafe","friction","F"));
        list.add(new VarInsnNode(Opcodes.FSTORE,3+ASTRAFE));

        ArrayList<AbstractInsnNode> rl=new ArrayList<>();
        //ArrayList<AbstractInsnNode> rload=new ArrayList<>();
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode node = methodNode.instructions.get(i);
            if(node instanceof VarInsnNode&&((VarInsnNode) node).var>=3+ASTRAFE+j){
                ((VarInsnNode) node).var+=j;//插入偏移值;
            }
            if(node instanceof FieldInsnNode&&((FieldInsnNode) node).name.equals(Mappings.getObfField("field_70177_z"))){
                 //标记替换yaw轴
                AbstractInsnNode aload_0 = methodNode.instructions.get(i-1);
                if(aload_0 instanceof VarInsnNode){
                    ((VarInsnNode) aload_0).var=4+ASTRAFE;
                    rl.add(node);
                }

            }

        }
        methodNode.instructions.insert(list);
        int bound = rl.size();
        for (int x = 0; x < bound; x++) {
                AbstractInsnNode node = rl.get(x);
                methodNode.instructions.insert(node, new FieldInsnNode(Opcodes.GETFIELD, "com/fun/eventapi/event/events/EventStrafe", "yaw", "F"));
                methodNode.instructions.remove(node);
        }





    }
    /*
    public void moveRelative(float strafe, float up, float forward, float friction)
    {
        float f = strafe * strafe + up * up + forward * forward;

        if (f >= 1.0E-4F)
        {
            f = MathHelper.sqrt(f);

            if (f < 1.0F)
            {
                f = 1.0F;
            }

            f = friction / f;
            strafe = strafe * f;
            up = up * f;
            forward = forward * f;
            float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F);
            this.motionX += (double)(strafe * f2 - forward * f1);
            this.motionY += (double)up;
            this.motionZ += (double)(forward * f2 + strafe * f1);
        }
    }

     */
    public static EventStrafe onStrafe(Object entity,float f0, float f1, float f2, float f){
        //System.out.println("onStrafe1");
        //System.out.println("onStrafe");
        EventStrafe eventStrafe=new EventStrafe(f1,f0,f,f2);
        if(entity.getClass().getName().equals(Mappings.getObfClass("net/minecraft/client/entity/EntityPlayerSP").replace('/','.'))) EventManager.call(eventStrafe);
        return eventStrafe;
    }
    /*
       public void a(float var1, float var2, float var3) {
        float var4 = var1 * var1 + var2 * var2;
        if (!(var4 < 1.0E-4F)) {
            var4 = ns.c(var4);
            if (var4 < 1.0F) {
                var4 = 1.0F;
            }

            var4 = var3 / var4;
            var1 *= var4;
            var2 *= var4;
            float var5 = ns.a(this.y * 3.1415927F / 180.0F);
            float var6 = ns.b(this.y * 3.1415927F / 180.0F);
            this.v += (double)(var1 * var6 - var2 * var5);
            this.x += (double)(var2 * var6 + var1 * var5);
        }
    }

     */
}
