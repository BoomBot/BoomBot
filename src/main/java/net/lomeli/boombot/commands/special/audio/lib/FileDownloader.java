package net.lomeli.boombot.commands.special.audio.lib;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;

import net.lomeli.boombot.helper.Logger;

public class FileDownloader implements Runnable {
    private File file;
    private URL fileURL;
    private long size;
    private boolean finished;

    public FileDownloader(File file, URL url, long size) {
        this.file = file;
        this.fileURL = url;
        this.size = size;
    }
    @Override
    public void run() {
        try {
            if (downloadResource(fileURL, file, size)) {
                Logger.debug("Finished Download file");
                finished = true;
            }
        } catch (Exception ex) {
            Logger.error("Failed to download file from %s", ex, fileURL.toString());
        }
    }

    public boolean isFinished() {
        return finished;
    }

    private boolean downloadResource(URL urlSource, File destination, long size) throws IOException {
        if (destination.exists()) {
            if (destination.length() == size)
                return false;
        } else if (destination.getParentFile() != null && !destination.getParentFile().exists())
            destination.getParentFile().mkdirs();
        byte[] buffer = new byte[4096];
        InputStream input = urlSource.openStream();
        FileOutputStream output = FileUtils.openOutputStream(destination);
        int n = 0;
        try {
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }
        return true;
    }
}
