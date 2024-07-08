package com.fun.inject.injection.asm.transformers;

import com.fun.client.command.CommandHandle;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Transformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class GuiScreenTransformer extends Transformer {
    public GuiScreenTransformer() {
        super("net/minecraft/client/gui/GuiScreen");
    }
    @Inject(method = "sendChatMessage",descriptor = "(Ljava/lang/String;Z)V")
    public void onMessage(MethodNode methodNode){
        InsnList list=new InsnList();
        LabelNode label=new LabelNode();
        list.add(new VarInsnNode(Opcodes.ALOAD,1));
        //list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL,Type.getInternalName(String.class),"toString","()Ljava/lang/String;"));
        //list.add(new LdcInsnNode(false));//
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(GuiScreenTransformer.class),"handler","(Ljava/lang/String;)Z"));
        list.add(new JumpInsnNode(Opcodes.IFEQ,label));
        //Opcodes.INVOKE
        //methodNode.visitMaxs(0,0);
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(label);

        methodNode.instructions.insert(list);
    }
    public static boolean handler(String msg){
        return CommandHandle.onSendMessage(msg);
    }
}
