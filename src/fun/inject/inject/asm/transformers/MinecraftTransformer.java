package fun.inject.inject.asm.transformers;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.event.events.EventTick;
import fun.inject.inject.asm.api.Mixin;
import fun.inject.inject.asm.api.Transformer;
import fun.utils.Classes;
import fun.utils.Methods;
import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class MinecraftTransformer extends Transformer {
    public MinecraftTransformer() {
        super("net/minecraft/client/Minecraft");
    }
    @Mixin(method = Methods.runTick_Minecraft)
    public void runTick(MethodNode methodNode){
        methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftTransformer.class),"onRunTick","()V"));
    }
    public static void onRunTick(){
        EventManager.call(new EventTick());
    }
}
