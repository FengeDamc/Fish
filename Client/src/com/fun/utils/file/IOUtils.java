package com.fun.utils.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IOUtils {
    public static InputStream getEntryFromJar(JarFile jar,String entryName) {
        try {
            JarEntry entry = jar.getJarEntry(entryName);
            if (entry != null) {
                //System.out.println("Entry found in JAR: " + entryName);
                InputStream in = jar.getInputStream(entry);

                return in;
            } else {
                System.out.println("Entry not found in JAR: " + entryName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
