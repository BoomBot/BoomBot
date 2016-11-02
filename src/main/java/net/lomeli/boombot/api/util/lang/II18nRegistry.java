package net.lomeli.boombot.api.util.lang;

public interface II18nRegistry {
    boolean hasLang(String lang);

    I18n getLang(String lang);

    void loadLangFolder(String path);

    void appendLang(String path);

    void reloadLangFolder(String path);
}
