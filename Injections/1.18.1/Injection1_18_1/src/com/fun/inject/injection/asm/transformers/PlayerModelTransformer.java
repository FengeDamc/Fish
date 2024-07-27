package com.fun.inject.injection.asm.transformers;

import com.fun.client.FunGhostClient;
import com.fun.inject.injection.asm.api.Inject;
import com.fun.inject.injection.asm.api.Transformer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class PlayerModelTransformer extends Transformer {
    public PlayerModelTransformer() {
        super("net/minecraft/client/model/PlayerModel");
    }////MD: net/minecraft/client/model/PlayerModel/m_6973_ (Lnet/minecraft/world/entity/Entity;FFFFF)V net/minecraft/client/model/PlayerModel/setupAnim (Lnet/minecraft/world/entity/Entity;FFFFF)V
    //MD: net/minecraft/client/model/PlayerModel/m_6973_ (Lnet/minecraft/world/entity/LivingEntity;FFFFF)V net/minecraft/client/model/PlayerModel/setupAnim (Lnet/minecraft/world/entity/LivingEntity;FFFFF)V


    @Inject(method = "setupAnim",descriptor = "(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V")
    public void setupAnim(MethodNode methodNode){
        System.out.println("hook playerModel");
        AbstractInsnNode node=methodNode.instructions.get(methodNode.instructions.size()-1);
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode n = methodNode.instructions.get(i);
            if(n.getOpcode()==Opcodes.RETURN)node=n;
        }
        InsnList list=new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD,0));
        list.add(new VarInsnNode(Opcodes.ALOAD,1));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(PlayerModelTransformer.class),"onSetupAnim","(Ljava/lang/Object;Ljava/lang/Object;)V"));
        methodNode.instructions.insertBefore(node,list);


    }
    public static void onSetupAnim(Object model,Object entity){
        if(FunGhostClient.rotationManager.isActive()
                &&FunGhostClient.registerManager.vModuleManager.rotations.isRunning()
                &&entity instanceof LocalPlayer&&model instanceof PlayerModel){
            ((HumanoidModel<?>) model).head.xRot= (float) (FunGhostClient.rotationManager.getRation().x/(180.0/Math.PI));
        }
    }
}
