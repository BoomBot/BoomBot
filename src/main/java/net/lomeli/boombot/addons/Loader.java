package net.lomeli.boombot.addons;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.addons.discovery.AddonCandidate;
import net.lomeli.boombot.addons.discovery.AddonLoader;
import net.lomeli.boombot.addons.exceptions.DuplicateAddonException;
import net.lomeli.boombot.api.BoomAddon;
import net.lomeli.boombot.api.BoomBotAPI;
import net.lomeli.boombot.helper.AddonHelper;
import net.lomeli.boombot.logging.BoomLogger;

/**
 * Slightly based of FML
 */
public class Loader {
    private AddonClassLoader addonClassLoader;
    private AddonLoader addonLoader;

    public Loader() {
        BoomLogger.info("Creating addon class loader");
        addonClassLoader = new AddonClassLoader(BoomAddon.class.getClassLoader());
        addonLoader = new AddonLoader();
    }

    public void loadAddons() {
        BoomLogger.info("Discovering addons");
        discoverAddons();
        addonLoader.findAddons();
        try {
            addonLoader.searchForDuplicates();
        } catch (DuplicateAddonException ex) {
            BoomLogger.error("", ex);
            BoomBot.shutdownBoomBot();
        }
        BoomLogger.info("Initializing addons");
        addonLoader.initAddons();
        BoomLogger.info("Finished Initializing addons");
        BoomLogger.info("Post-initializing addons");
        addonLoader.postAddons();
        BoomLogger.info("Finished Post-Initializing of addons");
    }

    private void discoverAddons() {
        if (BoomBot.debug)
            discoverClassPathAddons();
        discoverAddonsFromFolder();
    }

    private void discoverClassPathAddons() {
        BoomLogger.debug("Loading debug addons");
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
        BoomLogger.info("Looking for addons in addons folder");
        if (!BoomBotAPI.ADDON_FOLDER.exists() || !BoomBotAPI.ADDON_FOLDER.isDirectory()) {
            BoomBotAPI.ADDON_FOLDER.mkdir();
            BoomBotAPI.ADDON_CONFIG_FOLDER.mkdir();
            return;
        }

        if (!BoomBotAPI.ADDON_CONFIG_FOLDER.exists() || !BoomBotAPI.ADDON_CONFIG_FOLDER.isDirectory())
            BoomBotAPI.ADDON_CONFIG_FOLDER.mkdir();

        File[] files = BoomBotAPI.ADDON_FOLDER.listFiles(new FilenameFilter() {
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

