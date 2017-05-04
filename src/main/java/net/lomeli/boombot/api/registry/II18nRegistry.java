package net.lomeli.boombot.api.registry;

import java.io.File;

import net.lomeli.boombot.api.lib.I18n;

public interface II18nRegistry {
    boolean hasLang(String langID);

    I18n getLang(String langID);

    void loadLangFolder(String addonID, String path);

    void appendLang(String path);

    void reloadLangFolder(String addonID, String path);
}
