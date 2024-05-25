package fun.inject.inject.asm.transformers;

import fun.inject.inject.Mappings;
import fun.inject.inject.ViaVersionHelper;
import fun.inject.inject.asm.api.Inject;
import fun.inject.inject.asm.api.Transformer;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.RETURN;

public class NetworkHandlerTransformer extends Transformer {
    public NetworkHandlerTransformer() {
        super("net/minecraft/client/network/NetHandlerPlayClient");
    }


    @Inject(method = "func_147297_a",descriptor = "net/minecraft/network/Packet")
    public void sendPacket(MethodNode mn) {
        InsnList list = new InsnList();
        LabelNode label = new LabelNode();

        list.add(new VarInsnNode(ALOAD, 1));
        //list.add(new InsnNode(ACONST));
        list.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(NetworkManagerTransFormer.class),"onPacket","(Ljava/lang/Object;)Z"));
        list.add(new JumpInsnNode(IFEQ, label));
        list.add(new InsnNode(RETURN));
        list.add(label);
        mn.instructions.insert(list);
    }
}
