package fun.inject.inject.asm.api;

import fun.inject.Native;
import fun.inject.inject.Mappings;
import fun.inject.inject.asm.FishClassWriter;
import fun.inject.inject.asm.transformers.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.instrument.ClassDefinition;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class Transformers {

    public static final Logger logger = LogManager.getLogger("Transformers");

    public static final List<Transformer> transformers = new ArrayList<>();
    public static boolean contains(String[] s1,String target){
        for(String s:s1){
            if(!target.replace('.', '/').contains(Mappings.getObfClass(s).replace('.', '/')))return false;
        }
        return true;
    }


    public static void transform(Native inst) throws IOException {

        if (transformers.isEmpty()) {
            logger.warn("No transformers were added");
            return;
        }

        logger.info("{} transformers to load", transformers.size());

        for (Transformer transformer : transformers) {
            if (transformer.getClazz() == null) {
                logger.warn("Class for {} ({}) is null", transformer.getObfName(), transformer.getName());
                continue;
            }

            byte[] bytes = transformer.getOldBytes();
            ClassNode node = node(bytes);
            if (node == null) {
                logger.error("Class node could not be created from {}", transformer.getClazz());
                continue;
            }

            for (Method method : transformer.getClass().getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Inject.class)) continue;

                if (method.getParameterCount() != 1 || !MethodNode.class.isAssignableFrom(method.getParameterTypes()[0]))
                    continue;

                Inject inject = method.getAnnotation(Inject.class);

                String methodToModify = inject.method();
                String desc = inject.descriptor();

                String obfName = Mappings.getObfMethod(methodToModify);
                if (obfName == null || obfName.isEmpty()) {
                    logger.error("Could not find {} in class {}", methodToModify, transformer.getName());
                    continue;
                }


                // huh???
                for (MethodNode mNode : node.methods) {
                    if (mNode.name.equals(obfName) && (contains(desc.split(" "),mNode.desc)|| desc.isEmpty())) {
                        try {
                            method.invoke(transformer, mNode);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            logger.error("Failed to invoke method {} {}", e.getMessage(), e.getStackTrace()[0]);
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }


                bytes = rewriteClass(node);

                //logger.error("Could not rewrite class bytes for {} ({}) by {} {}", transformer.getClazz(), transformer.getName(),e.getCause().toString(),e.getStackTrace());
                //logger.error(e.getMessage() + " -> ");



            if (bytes.length != 0) {

                ClassDefinition classDef = new ClassDefinition(transformer.getClazz(), bytes.clone());
                try {
                    //File f=new File(transformer.getName()+".class");
                    File fo=new File(transformer.getName()+".class");

                    //FileUtils.writeByteArrayToFile(f,Mappings.deobfClass(bytes).clone());
                    FileUtils.writeByteArrayToFile(fo,bytes.clone());

                    //logger.info(f.getAbsolutePath());
                    logger.info(fo.getAbsolutePath());
                    logger.info("Redefined class {} ({}) ({}) ({} bytes)", transformer.getObfName(),transformer.getOldBytes().length, transformer.getName(), bytes.length);
                    inst.redefineClass(transformer.getClazz(), bytes.clone());



                } catch (Exception e) {
                    logger.error("Failed to modify {} ({})", transformer.getObfName(), transformer.getName());
                    logger.error(e.getMessage() + " -> " + e.getStackTrace()[0]);
                    e.printStackTrace();
                }
            }
        }
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
