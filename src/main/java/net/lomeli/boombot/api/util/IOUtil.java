package net.lomeli.boombot.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import net.lomeli.boombot.api.BoomAPI;

public class IOUtil {
    /**
     * Used to compress files into Gzip format
     */
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
            BoomAPI.logger.info("Finished gzipping %s", source.getName());
        } catch (IOException ex) {
            BoomAPI.logger.error("Failed to gzip file.", ex);
        }
    }
}
