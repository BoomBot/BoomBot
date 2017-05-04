package net.lomeli.boombot.core.registry;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.lib.I18n;
import net.lomeli.boombot.api.registry.II18nRegistry;

public class I18nRegistry implements II18nRegistry {
    private static final File ASSETS_FOLDER = new File("assets");
    private Map<String, I18n> translations;

    public I18nRegistry() {
        translations = Maps.newHashMap();
    }

    @Override
    public boolean hasLang(String langID) {
        return translations.containsKey(langID.toLowerCase());
    }

    @Override
    public I18n getLang(String langID) {
        I18n lang = translations.get(langID.toLowerCase());
        if (lang == null) {
            lang = new I18n(langID.toLowerCase());
            translations.put(langID.toLowerCase(), lang);
        }
        return lang;
    }

    @Override
    public void loadLangFolder(String addonID, File addon) {
        BoomBot.logger.debug("Looking for %s's lang folder", addonID);
        try {
            JarFile jar = new JarFile(addon);
            Enumeration<JarEntry> jarEnum = jar.entries();
            while (jarEnum.hasMoreElements()) {
                JarEntry file = jarEnum.nextElement();
                if (file.isDirectory() || !file.getName().endsWith(".lang")) continue;
                String langName = FilenameUtils.getBaseName(file.getName());
                if (!Strings.isNullOrEmpty(langName)) {
                    I18n lang = getLang(langName);
                    InputStream stream = jar.getInputStream(file);
                    lang.loadTranslations(stream);
                    BoomBot.logger.debug("Adding %s's %s localizations", addonID, langName);
                    translations.put(langName.toLowerCase(), lang);
                }
            }
            jar.close();
        } catch (IOException ex) {
            BoomBot.logger.error("Could not open %s for localizations!", ex, addonID);
        }
    }

    public void loadAssetLang() {
        if (!ASSETS_FOLDER.exists() || !ASSETS_FOLDER.isDirectory()) ASSETS_FOLDER.mkdir();
        BoomBot.addonLoader.getAddons().keySet().stream().forEach(name -> {
            File f0 = new File(ASSETS_FOLDER, name);
            if (f0.exists() && f0.isDirectory()) {
                File f1 = new File(f0, "lang");
                if (f1.exists() && f1.isDirectory()) {
                    File[] files = f1.listFiles(
                            (dir, filename) -> FilenameUtils.isExtension(filename, "lang"));
                    if (files != null && files.length > 0) {
                        for (File file : files) {
                            String id = FilenameUtils.getBaseName(file.getPath());
                            I18n lang = getLang(id);
                            lang.loadTranslations(file);
                            translations.put(id.toLowerCase(), lang);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void appendLang(String path) {
    }

    @Override
    public void reloadLangFolder(String id, File addon) {
        translations.clear();
        loadLangFolder(id, addon);
    }
}
