package net.lomeli.boombot.api.registry;

import java.io.File;

import net.lomeli.boombot.api.lib.I18n;

public interface II18nRegistry {
    /**
     * @param langID - We use IETF language tag, but using
     *               '_' instead of '-'
     * @return true if localization exists
     */
    boolean hasLang(String langID);

    /**
     * @param langID - We use IETF language tag, but using
     *               '_' instead of '-'
     * @return the localization for the given language
     */
    I18n getLang(String langID);

    void loadLangFolder(String addonID, File addon);

    void reloadLangFolder(String addonID, File addon);
}
