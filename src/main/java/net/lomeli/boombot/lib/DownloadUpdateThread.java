package net.lomeli.boombot.lib;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

import net.lomeli.boombot.helper.Logger;
import net.lomeli.boombot.update.UpdateUtil;

public class DownloadUpdateThread implements Runnable {
    @Override
    public void run() {
        try {
            File updateFile = new File("update.zip");
            UpdateUtil.getRemoteData();
            if (UpdateUtil.updateAvailable()) {
                if (updateFile.exists())
                    updateFile.delete();
                FileUtils.copyURLToFile(new URL(UpdateUtil.downloadURL()), updateFile);

            }
        } catch (Exception e) {
            Logger.error("Failed to download update.", e);
        }
    }
}
