package net.lomeli.boombot.addons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import net.lomeli.boombot.helper.Logger;

public class AddonLoader {
    private static final File addonFolder = new File("addons");
    private static BoomClassLoader classLoader;

    public static void loadAddons() {
        Logger.info("Creating addon class loader");
        classLoader = new BoomClassLoader();
        Logger.info("Locating addons folder");
        if (!addonFolder.exists())
            addonFolder.mkdir();
        else if (!addonFolder.isDirectory())
            addonFolder.mkdir();
        File[] files = addonFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar") || name.endsWith(".zip");
            }
        });
        if (files != null && files.length > 0) {
            for (File file : files) {
                try {
                    classLoader.addFile(file);
                } catch (FileNotFoundException ex) {
                    Logger.error("Could not find file %s", ex, file.getName());
                }
            }
            if (classLoader.addonsFound()) {
                Logger.info("Found potential addons. Looking for and loading addon classes.");
                classLoader.loadMainClass();
            }
        }

    }
}
