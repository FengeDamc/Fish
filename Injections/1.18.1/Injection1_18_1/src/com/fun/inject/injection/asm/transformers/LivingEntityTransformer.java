package com.fun.inject.injection.asm.transformers;

import com.fun.eventapi.EventManager;
import com.fun.eventapi.event.events.EventJump;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Transformer;
import com.fun.inject.mapper.Mapper;
import net.minecraft.world.entity.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class LivingEntityTransformer extends Transformer {
    public LivingEntityTransformer() {
        super("net/minecraft/world/entity/LivingEntity");
    }
    @Inject(method = "jumpFromGround",descriptor = "()V")
    public void jumpFromGround(MethodNode methodNode){
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode node = methodNode.instructions.get(i);

            if (node instanceof MethodInsnNode && ((MethodInsnNode) node).name.equals(Mapper.getObfMethod("getYRot", "net/minecraft/world/entity/Entity", "()F"))
                    && ((MethodInsnNode) node).desc.equals("()F")) {
                //替换yaw轴
                methodNode.instructions.insertBefore(node,new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(LivingEntityTransformer.class),"onJump","(Ljava/lang/Object;)F"));

                methodNode.instructions.remove(node);

            }
        }
    }
    public static float onJump(Object entity){
        return ((EventJump)EventManager.call(new EventJump(((LivingEntity)entity).getYRot()))).yaw;
    }

}
