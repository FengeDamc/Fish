package com.fun.inject.injection.asm.transformers;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventKey;
import com.fun.inject.Mappings;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Transformer;
import com.fun.inject.mapper.Mapper;

import com.mojang.blaze3d.platform.InputConstants;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class KeyBindTransformer extends Transformer {
    public KeyBindTransformer() {
        super("net/minecraft/client/KeyMapping");
    }
    @Inject(method = "click",descriptor = "(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V")
    public void onKey(MethodNode methodNode) {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD,0));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,Mappings.getObfClass("com/mojang/blaze3d/platform/InputConstants$Key"), Mappings.getObfMethod("m_84873_"),"()I"));
        //MD: com/mojang/blaze3d/platform/InputConstants$Key/m_84873_ ()I com/mojang/blaze3d/platform/InputConstants$Key/getValue ()I
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(KeyBindTransformer.class),"onKey", Mapper.getObfMethodDesc("(I)V")));
        methodNode.instructions.insert(list);



    }
    public static void onKey(int key){
        EventManager.call(new EventKey(key));
    }
}
