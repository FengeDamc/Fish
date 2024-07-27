package com.fun.inject.injection.asm.transformers;


import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventPacket;
import com.fun.inject.Agent;
import com.fun.inject.Mappings;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Mixin;
import com.fun.inject.injection.asm.api.Transformer;
import com.fun.utils.version.methods.Methods;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.tree.*;

public class NetworkManagerTransFormer extends Transformer {
    public NetworkManagerTransFormer() {
        super("net/minecraft/network/Connection");
    }

    // MD: ek/a (Lio/netty/channel/ChannelHandlerContext;Lff;)V net/minecraft/network/NetworkManager/channelRead0 (Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V
    @Inject(method = "genericsFtw",descriptor = "(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;)V")
    public void channelRead0(MethodNode mn) {
        InsnList list = new InsnList();
        LabelNode label = new LabelNode();
        list.add(new VarInsnNode(ALOAD, 0));

        list.add(new MethodInsnNode(INVOKESTATIC,Type.getInternalName(NetworkManagerTransFormer.class),"onPacket","(Ljava/lang/Object;)Z"));
        list.add(new JumpInsnNode(IFEQ, label));
        list.add(new InsnNode(RETURN));

        list.add(label);
        mn.instructions.insert(list);


    }

    // func_179290_a,sendPacket,2,

    public static boolean onPacket(Object packet){
        //System.out.println(Mappings.getUnobfClass(packet.getClass().getName()));
        return EventManager.call(new EventPacket(packet)).cancel;
    }
}
