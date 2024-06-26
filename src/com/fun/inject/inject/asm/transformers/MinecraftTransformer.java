package com.fun.inject.inject.asm.transformers;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventTick;
import com.fun.inject.inject.asm.api.Mixin;
import com.fun.inject.inject.asm.api.Transformer;
import com.fun.utils.Classes;
import com.fun.utils.Methods;
import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class MinecraftTransformer extends Transformer {
    public MinecraftTransformer() {
        super(Classes.Minecraft);
    }
    @Mixin(method = Methods.runTick_Minecraft)
    public void runTick(MethodNode methodNode){
        methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftTransformer.class),"onRunTick","()V"));
    }
    public static void onRunTick(){
        EventManager.call(new EventTick());
    }
}
