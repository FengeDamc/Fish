package com.fun.inject.injection.asm.transformers;


import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventRender2D;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Transformer;
import com.fun.inject.injection.asm.api.Transformers;
import com.fun.inject.Agent;
import com.fun.inject.Mappings;
import com.fun.inject.MinecraftType;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class GuiIngameTransformer extends Transformer {
    //private static final String GL_STATE_MANAGER_NAME = "net/minecraft/client/renderer/GlStateManager";


    public GuiIngameTransformer() {
        super(!(Agent.minecraftType == MinecraftType.FORGE) ?"net/minecraft/client/gui/GuiIngame":"net/minecraftforge/client/GuiIngameForge");
    }


    @Inject(method = "func_175180_a", descriptor = "(F)V")
    public void renderGameOverlay(MethodNode methodNode) {
        InsnList list = new InsnList();

        AbstractInsnNode point = null;
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode aisn = methodNode.instructions.get(i);
            if (aisn instanceof MethodInsnNode) {
                // we're looking for GlStateManager.color(...)
                // we dont break because we want to do it after .color is invoked

                MethodInsnNode meth = (MethodInsnNode) aisn; // hehe

                if (meth.owner.equals(Mappings.getObfClass("net/minecraft/client/renderer/GlStateManager"))
                        // MD: bfl/c (FFFF)V net/minecraft/client/renderer/GlStateManager/func_179131_c (FFFF)V
                        && meth.name.equals(Mappings.getObfMethod("func_179131_c"))
                        && meth.desc.equals("(FFFF)V")) {

                    point = aisn;

                }
            }
        }

        if (point == null) {
            Transformers.logger.error("Failed to find last GlStateManager#color call in GuiInGame");
            return;
        }

        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(GuiIngameTransformer.class),"onRender2D","()V"));


        methodNode.instructions.insert(point, list);

    }
    public static void onRender2D(){


        EventManager.call(new EventRender2D());
    }
}
