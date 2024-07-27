package com.fun.inject.injection.asm.transformers;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventTick;
import com.fun.eventapi.event.events.EventView;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Mixin;
import com.fun.inject.injection.asm.api.Transformer;
import com.fun.utils.version.clazz.Classes;
import com.fun.utils.version.methods.Methods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class MinecraftTransformer extends Transformer {
    public MinecraftTransformer() {
        super("net/minecraft/client/Minecraft");
    }
    @Inject(method = "runTick",descriptor = "(Z)V")//Minecraft/runTick (Z)V
    public void runTick(MethodNode methodNode){
        methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftTransformer.class),"onRunTick","()V"));
    }
    public static void onRunTick(){
        EventManager.call(new EventTick());
    }
    @Inject(method = "getCameraEntity",descriptor = "()Lnet/minecraft/world/entity/Entity;")
    public void getRenderViewEntity(MethodNode methodNode){
        methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftTransformer.class),"onGetRenderViewEntity","()V"));
    }

    public static void onGetRenderViewEntity(){
        EventManager.call(new EventView());
    }
}
