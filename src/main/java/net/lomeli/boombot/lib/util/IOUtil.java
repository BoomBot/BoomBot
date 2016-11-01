package net.lomeli.boombot.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class IOUtil {

    public static void gzipFile(File source, File target) {
        byte[] buffer = new byte[1024];
        try {
            GZIPOutputStream gzos = new GZIPOutputStream(new FileOutputStream(target));
            FileInputStream stream = new FileInputStream(source);
            int len;
            while ((len = stream.read(buffer)) > 0)
                gzos.write(buffer, 0, len);
            stream.close();
            gzos.finish();
            gzos.close();
            System.out.printf("Finished gzipping %s\n", source.getName());
        } catch (IOException ex) {
            System.out.println("Failed to gzip file.");
            ex.printStackTrace();
        }
    }
}
