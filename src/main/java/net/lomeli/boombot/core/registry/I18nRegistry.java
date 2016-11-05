package net.lomeli.boombot.core.registry;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.util.Map;

import net.lomeli.boombot.api.util.lang.I18n;
import net.lomeli.boombot.api.util.lang.II18nRegistry;

public class I18nRegistry implements II18nRegistry {
    private Map<String, I18n> translations;

    public I18nRegistry() {
        translations = Maps.newHashMap();
    }

    @Override
    public boolean hasLang(String lang) {
        return translations.containsKey(lang);
    }

    @Override
    public I18n getLang(String lang) {
        return translations.get(lang);
    }

    @Override
    public void loadLangFolder(String id, String path) {

    }

    @Override
    public void appendLang(String path) {
        String name = FilenameUtils.getBaseName(path);
        I18n lang = new I18n();
        if (hasLang(name)) lang = getLang(name);
    }

    @Override
    public void reloadLangFolder(String id, String path) {
        translations.clear();
        loadLangFolder(id, path);
    }
}
