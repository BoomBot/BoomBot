package net.lomeli.boombot.addons;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.addons.discovery.AddonCandidate;
import net.lomeli.boombot.addons.discovery.AddonLoader;
import net.lomeli.boombot.addons.exceptions.DuplicateAddonException;
import net.lomeli.boombot.api.BoomAddon;
import net.lomeli.boombot.helper.AddonHelper;
import net.lomeli.boombot.helper.Logger;

/**
 * Slightly based of FML
 */
public class Loader {
    private final File addonFolder = new File("addons");
    private AddonClassLoader addonClassLoader;
    private AddonLoader addonLoader;

    public Loader() {
        Logger.info("Creating addon class loader");
        addonClassLoader = new AddonClassLoader(BoomAddon.class.getClassLoader());
        addonLoader = new AddonLoader();
    }

    public void loadAddons() {
        Logger.info("Discovering addons");
        discoverAddons();
        addonLoader.findAddons();
        try {
            addonLoader.searchForDuplicates();
        } catch (DuplicateAddonException ex) {
            Logger.error("", ex);
            BoomBot.shutdownBoomBot();
        }
        addonLoader.initAddons();
    }

    private void discoverAddons() {
        if (BoomBot.debug)
            discoverClassPathAddons();
        discoverAddonsFromFolder();
    }

    private void discoverClassPathAddons() {
        Logger.debug("Loading debug addons");
        File[] sources = addonClassLoader.getParentSources();
        if (sources != null && sources.length > 0) {
            Arrays.sort(sources);
            for (File source : sources) {
                if (AddonHelper.ignoreFile(source.getAbsolutePath()))
                    continue;
                if (source.isFile())
                    addonLoader.addCandidate(new AddonCandidate(null, source, AddonCandidate.AddonType.JAR));
                else if (source.isDirectory())
                    addonLoader.addCandidate(new AddonCandidate(source, source, AddonCandidate.AddonType.DIR));
            }
        }
    }

    private void discoverAddonsFromFolder() {
        Logger.info("Looking for addons in addons folder");
        if (!addonFolder.exists()) {
            addonFolder.mkdir();
            return;
        } else if (!addonFolder.isDirectory()) {
            addonFolder.mkdir();
            return;
        }

        File[] files = addonFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar") || name.endsWith(".zip");
            }
        });
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.isFile())
                    addonLoader.addCandidate(new AddonCandidate(null, file, AddonCandidate.AddonType.JAR));
            }
        }
    }
}

