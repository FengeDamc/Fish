package com.fun.inject.inject.asm.transformers;

import com.fun.inject.inject.asm.api.Mixin;
import com.fun.inject.inject.asm.api.Transformer;
import com.fun.utils.Classes;
import com.fun.utils.Methods;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import static org.objectweb.asm.Opcodes.RETURN;

public class WYGameState extends Transformer {
    public WYGameState() {
        super(Classes.GameState);
    }
    @Mixin(method = Methods.needNewCheck)
    public void needNewCheck(MethodNode methodNode) {
        methodNode.instructions.clear();
        methodNode.instructions.add(new LdcInsnNode(false));
        methodNode.instructions.add(new InsnNode(RETURN));
    }
}
