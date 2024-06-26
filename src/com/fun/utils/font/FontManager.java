package com.fun.utils.font;



import com.fun.inject.Agent;
import com.fun.inject.inject.IOUtils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

public class FontManager {
    public static UnicodeFontRenderer inkFree;
    public static UnicodeFontRenderer tenacity;
    public static UnicodeFontRenderer tenacity20;
    public static UnicodeFontRenderer simkai;
    public static void init(){
        try(JarFile jar=new JarFile(Agent.jarPath);) {
            Font inkfree=getFont(35, IOUtils.getEntryFromJar(jar,"assets/fonts/Inkfree.ttf"));
            Font tenacityF=getFont(25,IOUtils.getEntryFromJar(jar,"assets/fonts/tenacity.ttf"));
            inkFree=new UnicodeFontRenderer(inkfree);
            tenacity=new UnicodeFontRenderer(tenacityF);
            tenacity20=new UnicodeFontRenderer(getFont(20,IOUtils.getEntryFromJar(jar,"assets/fonts/tenacity.ttf")));
            simkai=new UnicodeFontRenderer(getFont(25,IOUtils.getEntryFromJar(jar,"assets/fonts/simkai.ttf")));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static Font getFont(int size, InputStream is) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,is).deriveFont(Font.PLAIN, (float) size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }

}
