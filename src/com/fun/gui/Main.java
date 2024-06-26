package com.fun.gui;

import com.fun.inject.inject.InjectUtils;
import com.fun.inject.inject.WindowEnumerator;
import com.fun.inject.inject.dynamic.impl.gui.GuiScreenDynamic;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

public class Main {
    public static FishFrame fishFrame=null;
    public static void main(String[] args) {
        //FunGhostClient.init();
        try {
            FileUtils.writeByteArrayToFile(new File("./a.class"),new GuiScreenDynamic().dump());
        } catch (IOException e) {

        }
    }

}
