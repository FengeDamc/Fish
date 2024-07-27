import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {
    public static void main(String[] args) {
        String jarFilePath = Main.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        String destDirPath = ".";//System.getProperty("user.home");

        File destDir = new File(destDirPath,".fish");
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                System.err.println("Could not create destination directory: " + destDirPath);
                return;
            }
        }

        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if(!entry.getName().startsWith("fish"))continue;
                File destFile = new File(destDir, entry.getName());
                if (!entry.isDirectory()) {
                    extractFile(jarFile, entry, destFile);
                } else if (!destFile.exists()) {
                    if (!destFile.mkdirs()) {
                        System.err.println("Could not create directory: " + destFile);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Runtime.getRuntime().exec("java -jar ");
    }

    private static void extractFile(JarFile jarFile, JarEntry entry, File destFile) throws IOException {
        try (InputStream in = jarFile.getInputStream(entry);
             FileOutputStream out = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        }
    }
}