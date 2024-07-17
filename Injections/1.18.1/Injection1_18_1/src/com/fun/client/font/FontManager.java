package com.fun.client.font;



import com.fun.inject.Agent;
import com.fun.utils.file.IOUtils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

public class FontManager {
    public static FontRenderer inkFree;
    public static FontRenderer tenacity;
    public static FontRenderer tenacity20;
    public static FontRenderer simkai;
    public static void init(){
        try(JarFile jar=new JarFile(Agent.jarPath);) {
            Font inkfree=getFont(35, IOUtils.getEntryFromJar(jar,"assets/fonts/Inkfree.ttf"));
            Font tenacityF=getFont(25,IOUtils.getEntryFromJar(jar,"assets/fonts/tenacity.ttf"));
            inkFree=createFontRenderer(inkfree, 35);
            tenacity=createFontRenderer(tenacityF,25);
            tenacity20=createFontRenderer(getFont(20,IOUtils.getEntryFromJar(jar,"assets/fonts/tenacity.ttf")),20);
            simkai=createFontRenderer(getFont(25,IOUtils.getEntryFromJar(jar,"assets/fonts/simkai.ttf")),25);
            System.out.println("FontRenderer created");
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
    public static FontRenderer createFontRenderer(Font font,int size) {
        return new FontRenderer(font,size/2f);
    }

}
