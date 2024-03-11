package fun.inject;



import fun.client.FunGhostClient;
import fun.inject.inject.Mappings;
import fun.inject.inject.MinecraftType;
import fun.inject.inject.MinecraftVersion;
import fun.inject.inject.asm.api.Transformers;
import fun.inject.inject.dynamic.Dynamics;
import fun.inject.inject.wrapper.impl.MinecraftWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class Agent {
    public static Instrumentation instrumentation;
    public static Logger logger= LogManager.getLogger("FunClient");

    public static void agentmain(String args, Instrumentation is) {
        instrumentation=is;
        logger.info("注入成功!!!");
        try {
            Mappings.readMappings(MinecraftVersion.VER_189, MinecraftType.VANILLA);
            logger.info("Read mappings successfully:"+Mappings.obfClass);

        } catch (Exception e) {
            logger.info("Failed to read mappings,");
            e.printStackTrace();

        }
        //logger.info("test1");

        try {
            Dynamics.defineClasses();
            logger.info("Define dynamic classes successfully");
        } catch (Exception e) {
            logger.info("Failed to init dynamic classes {}, {}", e.getMessage(), e.getStackTrace()[0]);
            e.printStackTrace();
        }

        try {
            //Lepton.init(is);
            FunGhostClient.init();
            logger.info("Initialize client successfully");
        } catch (Exception e) {
            logger.info("Failed to initialize client {}, {}", e.getMessage(), e.getStackTrace()[0]);
        }

        try {
            Transformers.transform(is);
            logger.info("Transform classes successfully");
        } catch (IOException e) {
            logger.info("Failed to init transformers {}, {}", e.getMessage(), e.getStackTrace()[0]);
            e.printStackTrace();
        }

    }


}
