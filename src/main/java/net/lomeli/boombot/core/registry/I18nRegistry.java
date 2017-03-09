package net.lomeli.boombot.core.registry;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

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
        return translations.containsKey(langID);
    }

    @Override
    public I18n getLang(String langID) {
        return hasLang(langID) ? translations.get(langID) : new I18n(langID.toLowerCase());
    }

    @Override
    public void loadLangFolder(String addonID, String path) {
        BoomBot.logger.debug("Looking for %s's lang folder", addonID);
        if (!ASSETS_FOLDER.exists() || !ASSETS_FOLDER.isDirectory()) ASSETS_FOLDER.mkdir();
        File addonAssets = new File(ASSETS_FOLDER, addonID.toLowerCase());
        if (!addonAssets.exists() || !addonAssets.isDirectory()) return;
        File langAssets = new File(addonAssets, "lang");
        if (!langAssets.exists() || !langAssets.isDirectory()) return;
        BoomBot.logger.debug("Found lang folder for %s!", addonID);
        File[] possibleLangFiles = langAssets.listFiles(((dir, name) -> FilenameUtils.isExtension(name, "lang")));
        if (possibleLangFiles == null || possibleLangFiles.length < 1) return;
        List<File> langFiles = Lists.newArrayList(possibleLangFiles);
        langFiles.stream().forEach(file -> {
            String id = FilenameUtils.getBaseName(file.getName());
            if (!Strings.isNullOrEmpty(id)) {
                I18n lang = getLang(id);
                lang.loadTranslations(file);
                BoomBot.logger.debug("Adding %s's %s localizations", addonID, id);
                translations.put(id, lang);
            }
        });
    }

    @Override
    public void appendLang(String path) {
        //String name = FilenameUtils.getBaseName(path);
        //I18n lang = new I18n();
        //if (hasLang(name)) lang = getLang(name);

    }

    @Override
    public void reloadLangFolder(String id, String path) {
        translations.clear();
        loadLangFolder(id, path);
    }
}
