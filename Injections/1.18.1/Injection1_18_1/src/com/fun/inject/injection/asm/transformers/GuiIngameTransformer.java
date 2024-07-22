package com.fun.inject.injection.asm.transformers;


import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventRender2D;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Transformer;
import com.fun.inject.injection.asm.api.Transformers;
import com.fun.inject.Agent;
import com.fun.inject.Mappings;
import com.fun.inject.MinecraftType;
import com.fun.utils.RenderManager;
import com.mojang.blaze3d.vertex.PoseStack;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class GuiIngameTransformer extends Transformer {
    //private static final String GL_STATE_MANAGER_NAME = "net/minecraft/client/renderer/GlStateManager";


    public GuiIngameTransformer() {
        super(!(Agent.minecraftType == MinecraftType.FORGE) ?"net/minecraft/client/gui/Gui":"net/minecraftforge/client/gui/ForgeIngameGui");
    }


    @Inject(method = "render", descriptor = "(Lcom/mojang/blaze3d/vertex/PoseStack;F)V")
    public void render(MethodNode methodNode) {
        InsnList list = new InsnList();

        AbstractInsnNode point = null;
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode aisn = methodNode.instructions.get(i);
            if (aisn instanceof MethodInsnNode) {
                // we're looking for GlStateManager.color(...)
                // we dont break because we want to do it after .color is invoked

                MethodInsnNode meth = (MethodInsnNode) aisn; // hehe

                if (meth.owner.equals(Mappings.getObfClass("com/mojang/blaze3d/systems/RenderSystem"))
                        // MD: bfl/c (FFFF)V net/minecraft/client/renderer/GlStateManager/func_179131_c (FFFF)V
                        && meth.name.equals(Mappings.getObfMethod("m_157429_"))
                        && meth.desc.equals("(FFFF)V")) {
                    point = aisn;

                }
            }
        }

        if (point == null) {
            Transformers.logger.error("Failed to find last GlStateManager#color call in GuiInGame");
            return;
        }
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(GuiIngameTransformer.class),"onRender2D","(Ljava/lang/Object;)V"));


        methodNode.instructions.insert(point, list);

    }
    public static void onRender2D(Object poseStack){
        if(!(poseStack instanceof PoseStack)) throw new RuntimeException("invalid poseStack");
        RenderManager.currentPoseStack= (PoseStack) poseStack;
        EventManager.call(new EventRender2D());
    }
}
