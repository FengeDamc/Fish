package com.fun.inject.inject.asm.transformers;


import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventAttackReach;
import com.fun.eventapi.event.events.EventBlockReach;
import com.fun.client.FunGhostClient;
import com.fun.inject.inject.asm.api.Inject;
import com.fun.inject.inject.asm.api.Transformer;
import com.fun.inject.inject.Mappings;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import static org.objectweb.asm.Opcodes.*;


public class EntityRendererTransformer extends Transformer {
    public EntityRendererTransformer() {
        super("net/minecraft/client/renderer/EntityRenderer");
    }

    @Inject(method = "func_175068_a", descriptor = "(IFJ)V")
    public void renderWorldPass(MethodNode methodNode) {
        AbstractInsnNode ldcNode = null;
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode a = methodNode.instructions.get(i);
            if (a instanceof MethodInsnNode) {
                MethodInsnNode m = (MethodInsnNode) a;
                if (m.owner.equals(Mappings.getObfClass("net/minecraft/profiler/Profiler"))
                        && m.name.equals(Mappings.getObfMethod("func_76318_c"))) { // endStartSection

                    ldcNode = a;
                }
            }
        }



        InsnList list = new InsnList();

        //list.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(Lepton.class), "getEventBus", "()Lcn/matrixaura/lepton/listener/bus/EventBus;", false));
        //list.add(new TypeInsnNode(NEW, Type.getInternalName(EventRender3D.class)));
        //list.add(new InsnNode(DUP));
        list.add(new VarInsnNode(FLOAD, 2));
        list.add(new MethodInsnNode(INVOKESTATIC,Type.getInternalName(FunGhostClient.class),"onRender3D","(F)V"));
        //list.add(new MethodInsnNode(INVOKESPECIAL, Type.getInternalName(EventRender3D.class), "<initFrame>", "(F)V", false));
        //list.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(EventBus.class), "dispatch", "(Ljava/lang/Object;)Z", false));
        //list.add(new InsnNode(POP));

        methodNode.instructions.insert(ldcNode, list);



    }

    @Inject(method = "func_78473_a", descriptor = "(F)V")
    public void getMouseOver(MethodNode methodNode) {

        InsnList list = new InsnList();
        LdcInsnNode ldc = null;
        MethodInsnNode min = null;
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode x = methodNode.instructions.get(i);
            if(x instanceof MethodInsnNode){//func_78757_d,getBlockReachDistance,0,player reach distance = 4F
                if(Mappings.getObfMethod("func_78757_d").equals(((MethodInsnNode) x).name)&&
                ((MethodInsnNode) x).owner.equals(Mappings.getObfClass("net/minecraft/client/multiplayer/PlayerControllerMP")))
                    min= (MethodInsnNode) x;
            }
            if (x instanceof LdcInsnNode) {
                LdcInsnNode t = (LdcInsnNode) x;

                if (t.cst instanceof Double && ((Double) t.cst) == 3.0) {
                    ldc = t;
                }
            }
        }

        if (ldc == null) return;
        methodNode.instructions.insert(min,new MethodInsnNode(INVOKESTATIC, Type.getInternalName(EntityRendererTransformer.class), "onBlockReach", "(F)F"));
        //methodNode.instructions.remove(min);
        methodNode.instructions.insert(ldc, new MethodInsnNode(INVOKESTATIC, Type.getInternalName(EntityRendererTransformer.class), "onAttackReach", "()D"));
//        methodNode.instructions.insert(ldc, new VarInsnNode(ALOAD, 23));
        methodNode.instructions.remove(ldc);


    }

    public static double onAttackReach(){
        EventAttackReach e=new EventAttackReach(3.0d);
        EventManager.call(e);
        return e.reach;
    }
    public static float onBlockReach(float f){
        EventBlockReach e=new EventBlockReach(f);
        EventManager.call(e);
        return (float) e.reach;
    }
}
