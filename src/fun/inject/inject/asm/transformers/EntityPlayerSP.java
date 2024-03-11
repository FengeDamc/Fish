package fun.inject.inject.asm.transformers;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.events.EventUpdate;
import fun.inject.inject.asm.api.Inject;
import fun.inject.inject.asm.api.Transformer;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.*;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class EntityPlayerSP extends Transformer {
    public EntityPlayerSP() {
        super("net/minecraft/client/entity/EntityPlayerSP");
    }
    @Inject(method = "func_70071_h_")
    public void onUpdate(MethodNode methodNode) {
        InsnList list = new InsnList();
        list.add(new TypeInsnNode(NEW, Type.getInternalName(EventUpdate.class)));
        list.add(new InsnNode(DUP));
        list.add(new MethodInsnNode(INVOKESPECIAL, Type.getInternalName(EventUpdate.class), "<init>", "()V", false));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EventManager.class),"call","(Lcom/darkmagician6/eventapi/events/Event;)Lcom/darkmagician6/eventapi/events/Event"));
        list.add(new InsnNode(POP));

        methodNode.instructions.insert(list);
    }
}
