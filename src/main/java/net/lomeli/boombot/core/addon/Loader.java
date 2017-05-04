package net.lomeli.boombot.core.addon;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.events.bot.InitEvent;
import net.lomeli.boombot.api.events.bot.PostInitEvent;
import net.lomeli.boombot.core.addon.discovery.AddonCandidate;
import net.lomeli.boombot.core.addon.discovery.AddonHelper;

public class Loader {
    private AddonClassLoader classLoader;
    private Map<String, AddonContainer> addons;

    public Loader() {
        BoomAPI.logger.info("Creating class loader");
        classLoader = new AddonClassLoader(Addon.class.getClassLoader());
        addons = Maps.newHashMap();
    }

    private void registerAddon(AddonContainer container) {
        if (container != null) addons.put(container.getAddonInfo().addonID().toLowerCase(), container);
    }

    public void loadAddons() {
        BoomAPI.logger.info("Looking for addons");
        discoverAddons();

        addons.values().stream().forEach(c -> {
            try {
                BoomAPI.logger.debug(c.getAddonInfo().addonID());
                c.loadResources();
                c.preInitAddon();
            } catch (Exception ex) {
                BoomAPI.logger.error("Failed to load addon: %s", ex, c.getAddonInfo().addonID());
                System.exit(1);
            }
        });
    }

    private void discoverAddons() {
        discoverJars();
        if (BoomAPI.debugMode) discoverClasses();
    }

    private void discoverJars() {
        BoomAPI.logger.info("Looking for addons in addons folder");
        if (!BoomAPI.ADDON_FOLDER.exists() || !BoomAPI.ADDON_FOLDER.isDirectory()) {
            BoomAPI.ADDON_FOLDER.mkdir();
            return;
        }
        File[] files = BoomAPI.ADDON_FOLDER.listFiles((dir, name) -> FilenameUtils.isExtension(name, "jar") || FilenameUtils.isExtension(name, "zip"));
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isFile()) {
                    AddonCandidate candidate = new AddonCandidate(f, true);
                    List<AddonContainer> addons = candidate.findAddons();
                    if (addons != null && addons.size() > 0)
                        addons.stream().forEach(addon -> this.addons.put(addon.getAddonInfo().addonID().toLowerCase(), addon));
                }
            }
        }
    }

    private void discoverClasses() {
        BoomAPI.logger.debug("Loading debugging addons");
        File[] sources = classLoader.getParentSources();
        if (sources == null || sources.length <= 0) return;
        Arrays.sort(sources);
        for (File f : sources) {
            if (AddonHelper.ignoreFile(f.getAbsolutePath())) continue;
            AddonCandidate candidate = null;
            if (f.isFile()) candidate = new AddonCandidate(f, true);
            else if (f.isDirectory()) candidate = new AddonCandidate(f, false);
            if (candidate != null) {
                List<AddonContainer> addons = candidate.findAddons();
                if (addons != null && addons.size() > 0)
                    addons.stream().forEach(addon -> this.addons.put(addon.getAddonInfo().addonID().toLowerCase(), addon));
            }
        }
    }

    public void initAddons(InitEvent event) {
        addons.values().stream().forEach(c -> {
            try {
                c.initAddon(event);
            } catch (Exception ex) {
                BoomAPI.logger.error("Failed to load addon", ex);
                System.exit(1);
            }
        });
    }

    public void postInitAddon(PostInitEvent event) {
        addons.values().stream().forEach(c -> {
            try {
                c.postInitAddon(event);
            } catch (Exception ex) {
                BoomAPI.logger.error("Failed to load addon", ex);
                System.exit(1);
            }
        });
    }
}
