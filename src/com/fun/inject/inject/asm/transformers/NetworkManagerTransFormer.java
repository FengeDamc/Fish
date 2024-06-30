package com.fun.inject.inject.asm.transformers;


import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventPacket;
import com.fun.inject.Agent;
import com.fun.inject.inject.asm.api.Mixin;
import com.fun.inject.inject.asm.api.Transformer;
import com.fun.utils.version.methods.Methods;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class NetworkManagerTransFormer extends Transformer {
    public NetworkManagerTransFormer() {
        super("net/minecraft/network/NetworkManager");
    }

    // MD: ek/a (Lio/netty/channel/ChannelHandlerContext;Lff;)V net/minecraft/network/NetworkManager/channelRead0 (Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V
    @Mixin(method = Methods.channelRead0_NetworkManager)
    public void channelRead0(MethodNode mn) {
        Agent.logger.info("channelRead0: {} {}",Methods.channelRead0_NetworkManager.getName(),
                Methods.channelRead0_NetworkManager.getDescriptor());
        InsnList list = new InsnList();
        LabelNode label = new LabelNode();
        AbstractInsnNode varNode=null;
        for (int i = 0; i < mn.instructions.size(); ++i) {
            AbstractInsnNode node = mn.instructions.get(i);

            if (node instanceof VarInsnNode&&((VarInsnNode) node).var==2) {

                list.add(new VarInsnNode(ALOAD, 2));
                //list.add(new InsnNode(ICONST_0));
                varNode=node;
                list.add(new MethodInsnNode(INVOKESTATIC,Type.getInternalName(NetworkManagerTransFormer.class),"onPacket","(Ljava/lang/Object;)Z"));
                list.add(new JumpInsnNode(IFEQ, label));
                list.add(new InsnNode(RETURN));

                list.add(label);
                break;

            }
        }
        mn.instructions.insert(varNode,list);


    }
    /*@Mixin(method = Methods.channelRead0NoEvent_NetworkManager)
    public MethodNode channelRead0NoEvent(ClassNode n)
    {
        for(MethodNode mn : n.methods){
            if(mn.name.equals(Methods.channelRead0_NetworkManager.getName())&&
            mn.desc.equals(Methods.channelRead0_NetworkManager.getDescriptor())){
                MethodNode newNode=new MethodNode(mn.access,"channelRead0NoEvent",mn.desc,null,null);
                newNode.instructions.add(mn.instructions);
                return newNode;
            }
        }
        return null;
    }*/

    // func_179290_a,sendPacket,2,

    public static boolean onPacket(Object packet){
        //Agent.logger.info(Mappings.getUnobfClass(packet.getClass().getName()));
        return EventManager.call(new EventPacket(packet)).cancel;
    }
}
