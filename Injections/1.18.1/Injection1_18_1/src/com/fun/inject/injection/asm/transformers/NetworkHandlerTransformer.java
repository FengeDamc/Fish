package com.fun.inject.injection.asm.transformers;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventPacket;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Transformer;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.RETURN;
import org.objectweb.asm.tree.*;

public class NetworkHandlerTransformer extends Transformer {
    public NetworkHandlerTransformer() {
        super("net/minecraft/client/network/NetHandlerPlayClient");
    }


    @Inject(method = "sendPacket",descriptor = "(Lnet/minecraft/network/Packet;)V")
    public void sendPacket(MethodNode mn) {
        InsnList list = new InsnList();
        LabelNode label = new LabelNode();

        list.add(new VarInsnNode(ALOAD, 1));
        //list.add(new InsnNode(ACONST));
        list.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(NetworkHandlerTransformer.class),"onPacket","(Ljava/lang/Object;)Z"));
        list.add(new JumpInsnNode(IFEQ, label));
        list.add(new InsnNode(RETURN));
        list.add(label);
        mn.instructions.insert(list);
    }
    public static boolean onPacket(Object packet){
        //Agent.logger.info(Mappings.getUnobfClass(packet.getClass().getName()));
        return EventManager.call(new EventPacket(packet)).cancel;
    }
}
