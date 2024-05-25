package fun.inject.inject.asm.transformers;


import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.event.events.EventPacket;
import fun.inject.inject.Mappings;
import fun.inject.inject.asm.api.Inject;
import fun.inject.inject.asm.api.Transformer;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

public class NetworkManagerTransFormer extends Transformer {
    public NetworkManagerTransFormer() {
        super("net/minecraft/network/NetworkManager");
    }

    // MD: ek/a (Lio/netty/channel/ChannelHandlerContext;Lff;)V net/minecraft/network/NetworkManager/channelRead0 (Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V
    @Inject(method = "channelRead0",descriptor = "ChannelHandlerContext")
    public void channelRead0(MethodNode mn) {
        InsnList list = new InsnList();
        LabelNode label = new LabelNode();
        AbstractInsnNode varNode=null;
        for (int i = 0; i < mn.instructions.size(); ++i) {
            AbstractInsnNode node = mn.instructions.get(i);

            if (node instanceof VarInsnNode&&((VarInsnNode) node).var==1) {

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

    // func_179290_a,sendPacket,2,

    public static boolean onPacket(Object packet){
        return EventManager.call(new EventPacket(packet)).cancel;
    }
}
