package com.fun.inject.inject.asm.transformers;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventKey;
import com.fun.inject.inject.asm.api.Inject;
import com.fun.inject.inject.asm.api.Transformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class KeyBindTransformer extends Transformer {
    public KeyBindTransformer() {
        super("net/minecraft/client/settings/KeyBinding");
    }
    @Inject(method = "func_74507_a",descriptor = "(I)V")
    public void onKey(MethodNode methodNode) {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ILOAD,0));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(KeyBindTransformer.class),"onKey","(I)V"));
        methodNode.instructions.insert(list);


    }
    public static void onKey(int key){
        EventManager.call(new EventKey(key));
    }
}
