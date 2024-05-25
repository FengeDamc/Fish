package fun.inject.inject.asm.transformers;

import fun.inject.inject.asm.api.Transformer;

public class MinecraftTransformer extends Transformer {
    public MinecraftTransformer() {
        super("net/minecraft/client/Minecraft");
    }
}
