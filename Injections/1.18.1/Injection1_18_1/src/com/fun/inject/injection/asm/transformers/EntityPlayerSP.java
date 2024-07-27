package com.fun.inject.injection.asm.transformers;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventMoment;
import com.fun.eventapi.event.events.EventMotion;
import com.fun.eventapi.event.events.EventUpdate;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Mixin;
import com.fun.inject.injection.asm.api.Transformer;
import com.fun.inject.mapper.Mapper;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import com.fun.utils.version.runable.VRunable;
import com.fun.inject.Mappings;
import com.fun.inject.MinecraftVersion;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class EntityPlayerSP extends Transformer {
    public EntityPlayerSP() {
        super("net/minecraft/client/player/LocalPlayer");
    }

    public static EventMotion onMotion(double x, double y, double z, float yaw, float pitch) {
        return (EventMotion) EventManager.call(new EventMotion(x, y, z, yaw, pitch));
    }

    public static void onUpdateEvent() {
        //Agent.System.out.println("update");
        //Agent.System.out.println(EventManager.REGISTRY_MAP);
        EventManager.call(new EventUpdate());
    }
    //func_175161_p,onUpdateWalkingPlayer,0,called every tick when the player is on foot. Performs all the things that normally happen during movement.

    public static void onLivingUpdateEvent() {
        //Agent.System.out.println("livingupdate");
        //Agent.System.out.println(EventManager.REGISTRY_MAP);
        EventManager.call(new EventMoment());
    }
    /*
    public void p() {
        boolean var1 = this.aw();
        if (var1 != this.bQ) {
            if (var1) {
                this.a.a(new is(this, is.a.d));
            } else {
                this.a.a(new is(this, is.a.e));
            }

            this.bQ = var1;
        }

        boolean var2 = this.av();
        if (var2 != this.bP) {
            if (var2) {
                this.a.a(new is(this, is.a.a));
            } else {
                this.a.a(new is(this, is.a.b));
            }

            this.bP = var2;
        }

        if (this.A()) {
            double var3 = this.s - this.bK;     //posX-lastX
            double var5 = this.aR().b - this.bL;    //minY-lastY
            double var7 = this.u - this.bM;     //posZ-lastZ
            double var9 = (double)(this.y - this.bN);//yaw-lasYaw
            double var11 = (double)(this.z - this.bO);//pitch-lastPitch
            boolean var13 = var3 * var3 + var5 * var5 + var7 * var7 > 9.0E-4 || this.bR >= 20;
            boolean var14 = var9 != 0.0 || var11 != 0.0;
            if (this.m == null) {
                if (var13 && var14) {
                    this.a.a(new ip$b(this.s, this.aR().b, this.u, this.y, this.z, this.C));
                } else if (var13) {
                    this.a.a(new ip$a(this.s, this.aR().b, this.u, this.C));
                } else if (var14) {
                    this.a.a(new ip$c(this.y, this.z, this.C));
                } else {
                    this.a.a(new ip(this.C));
                }
            } else {
                this.a.a(new ip$b(this.v, -999.0, this.x, this.y, this.z, this.C));
                var13 = false;
            }

            ++this.bR;
            if (var13) {
                this.bK = this.s;
                this.bL = this.aR().b;
                this.bM = this.u;
                this.bR = 0;
            }

            if (var14) {
                this.bN = this.y;
                this.bO = this.z;
            }
        }

    }
    */

    //MD: bew/m ()V net/minecraft/client/entity/EntityPlayerSP/func_70636_d ()V onLivingUpdate
    @Inject(method = "aiStep" , descriptor = "()V")//@Mixin(method= Methods.onLivingUpdate_SP)
    public void onLivingUpdate(MethodNode methodNode) {
        InsnList list = new InsnList();
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EntityPlayerSP.class), "onLivingUpdateEvent", "()V"));
        MethodInsnNode target=null;
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode node = methodNode.instructions.get(i);
            if(node instanceof MethodInsnNode&& ((MethodInsnNode) node).name.equals(
                    Mappings.getObfMethod("m_120586_"))
            &&((MethodInsnNode) node).owner.equals(
                    Mappings.getObfClass("net/minecraft/client/tutorial/Tutorial"))){//
                target= (MethodInsnNode) node;
            }

        }

        methodNode.instructions.insert(target,list);


    }

    @Inject(method = "tick" , descriptor = "()V")//@Mixin(method=Methods.onUpdate_Entity)
    public void onUpdate(MethodNode methodNode) {
        InsnList list = new InsnList();
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EntityPlayerSP.class), "onUpdateEvent", "()V"));
        methodNode.instructions.insert(list);
        //BuiltinClassLoader

    }

    @Inject(method = "sendPosition",descriptor = "()V")//@Mixin(method = Methods.onUpdateWalking_SP)
    public void onUpdateWalking(MethodNode methodNode) {
        InsnList list = new InsnList();
        int j = 0;
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));//net.minecraft.world.entity.Entity
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                Mapper.getObfClass("net/minecraft/world/entity/Entity"), Mapper.getObfMethod("getX","net/minecraft/world/entity/Entity","()D"), "()D"));//"field_70165_t" Mapper.getObfClass("net/minecraft/world/entity/Entity")
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL , Mapper.getObfClass("net/minecraft/world/entity/Entity"), Mapper.getObfMethod("getY","net/minecraft/world/entity/Entity","()D"), "()D"));
        //field_70163_u,posY,2,Entity position Y
        //FD: pk/t net/minecraft/world/entity/Entity/field_70163_u
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL , Mapper.getObfClass("net/minecraft/world/entity/Entity"), Mapper.getObfMethod("getZ","net/minecraft/world/entity/Entity","()D"), "()D"));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,Mapper.getObfClass("net/minecraft/world/entity/Entity"), Mapper.getObfMethod("getYRot","net/minecraft/world/entity/Entity","()F"), "()F"));//yaw
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Mapper.getObfClass("net/minecraft/world/entity/Entity"), Mapper.getObfMethod("getXRot","net/minecraft/world/entity/Entity","()F"), "()F"));//pitch
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EntityPlayerSP.class), "onMotion", "(DDDFF)Lcom/fun/eventapi/event/events/EventMotion;"));
        list.add(new VarInsnNode(Opcodes.ASTORE, 1));
        j++;
        //ArrayList<AbstractInsnNode> rl=new ArrayList<>();
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode node = methodNode.instructions.get(i);
            if (node instanceof VarInsnNode && ((VarInsnNode) node).var >= j) {
                ((VarInsnNode) node).var += j;//插入偏移值;
            }

            if (node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals(Mapper.getObfMethod("getYRot","net/minecraft/world/entity/Entity","()F"))
                    &&((MethodInsnNode) node).desc.equals("()F")) {
                //标记替换yaw轴
                AbstractInsnNode aload_0 = methodNode.instructions.get(i - 1);
                if (aload_0 instanceof VarInsnNode) {
                    ((VarInsnNode) aload_0).var = 1;
                    methodNode.instructions.insert(node, new FieldInsnNode(Opcodes.GETFIELD, "com/fun/eventapi/event/events/EventMotion", "yaw", "F"));
                    methodNode.instructions.remove(node);
                    //rl.add(node);
                }
            }
            if (node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals(Mapper.getObfMethod("getXRot","net/minecraft/world/entity/Entity","()F"))
                    &&((MethodInsnNode) node).desc.equals("()F")) {
                //标记替换yaw轴
                AbstractInsnNode aload_0 = methodNode.instructions.get(i - 1);
                if (aload_0 instanceof VarInsnNode) {
                    ((VarInsnNode) aload_0).var = 1;
                    methodNode.instructions.insert(node, new FieldInsnNode(Opcodes.GETFIELD, "com/fun/eventapi/event/events/EventMotion", "pitch", "F"));
                    methodNode.instructions.remove(node);
                    //rl.add(node);
                }
            }
            if (node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals(Mapper.getObfMethod("getX","net/minecraft/world/entity/Entity","()D"))
                    &&((MethodInsnNode) node).desc.equals("()D")) {
                //标记替换yaw轴
                AbstractInsnNode aload_0 = methodNode.instructions.get(i - 1);
                if (aload_0 instanceof VarInsnNode) {
                    ((VarInsnNode) aload_0).var = 1;
                    methodNode.instructions.insert(node, new FieldInsnNode(Opcodes.GETFIELD, "com/fun/eventapi/event/events/EventMotion", "x", "D"));
                    methodNode.instructions.remove(node);
                    //rl.add(node);
                }
            }
            if (node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals(Mapper.getObfMethod("getY","net/minecraft/world/entity/Entity","()D"))
                    &&((MethodInsnNode) node).desc.equals("()D")) {
                //标记替换yaw轴
                AbstractInsnNode aload_0 = methodNode.instructions.get(i - 1);
                if (aload_0 instanceof VarInsnNode) {
                    ((VarInsnNode) aload_0).var = 1;
                    methodNode.instructions.insert(node, new FieldInsnNode(Opcodes.GETFIELD, "com/fun/eventapi/event/events/EventMotion", "y", "D"));
                    methodNode.instructions.remove(node);
                    //rl.add(node);
                }
            }
            if (node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals(Mapper.getObfMethod("getZ","net/minecraft/world/entity/Entity","()D"))
                    &&((MethodInsnNode) node).desc.equals("()D")) {
                //标记替换yaw轴
                AbstractInsnNode aload_0 = methodNode.instructions.get(i - 1);
                if (aload_0 instanceof VarInsnNode) {
                    ((VarInsnNode) aload_0).var = 1;
                    methodNode.instructions.insert(node, new FieldInsnNode(Opcodes.GETFIELD, "com/fun/eventapi/event/events/EventMotion", "z", "D"));
                    methodNode.instructions.remove(node);
                    //rl.add(node);
                }
            }




        }
        methodNode.instructions.insert(list);


    }
}
