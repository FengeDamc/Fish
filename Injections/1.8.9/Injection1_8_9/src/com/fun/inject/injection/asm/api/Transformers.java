package com.fun.inject.injection.asm.api;

import com.fun.inject.Mappings;
import com.fun.inject.utils.FishClassWriter;
import com.fun.inject.injection.asm.transformers.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class Transformers {

    public static Logger logger = LogManager.getLogger("Transformers");

    public static List<Transformer> transformers = new ArrayList<>();
    public static boolean contains(String[] s1,String target){
        for(String s:s1){
            if(!target.replace('.', '/').contains(Mappings.getObfClass(s).replace('.', '/')))return false;
        }
        return true;
    }




    public static ClassNode node(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            ClassReader reader = new ClassReader(bytes);
            ClassNode node = new ClassNode();
            reader.accept(node, ClassReader.EXPAND_FRAMES);
            return node;
        }

        return null;
    }

    public static byte[] rewriteClass(ClassNode node) {
        try {
            ClassWriter writer = new FishClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
            node.accept(writer);
            return writer.toByteArray();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void init(){
        Transformers.transformers.clear();
        transformers.add(new EntityPlayerSP());
//
        transformers.add(new GuiIngameTransformer());
        transformers.add(new NetworkManagerTransFormer());
        transformers.add(new NetworkHandlerTransformer());
        transformers.add(new EntityRendererTransformer());
        transformers.add(new KeyBindTransformer());
        transformers.add(new GuiScreenTransformer());
        transformers.add(new EntityTransformer());
        transformers.add(new MinecraftTransformer());


    }
}
