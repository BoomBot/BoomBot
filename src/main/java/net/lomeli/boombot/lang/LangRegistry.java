package net.lomeli.boombot.lang;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import net.lomeli.boombot.lib.Logger;

/**
 * Copied over from another project of mine, to be implemented at a future date.
 */
public class LangRegistry {
    private static Localization currentLang;
    private static Localization english;
    private static HashMap<String, Localization> localizationList;
    private static File langFolder = new File("lang");

    public static void initRegistry() {
        localizationList = Maps.newHashMap();
        try {
            if (langFolder.exists()) {
                File[] files = langFolder.listFiles();
                if (files != null && files.length > 0) {
                    for (File langFile : files) {
                        if (FilenameUtils.getExtension(langFile.getAbsolutePath()).equals("lang")) {
                            Localization local = new Localization(langFile);
                            local.parseFile();
                            localizationList.put(FilenameUtils.getBaseName(langFile.getAbsolutePath()), local);
                            if (FilenameUtils.getBaseName(langFile.getAbsolutePath()).equals("en_US"))
                                english = local;
                            Logger.info("Loaded " + local.getLocalizationName() + " localization");
                        }
                    }
                }
            }
            currentLang = english;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentLang(String id) {
        if (localizationList.containsKey(id))
            currentLang = localizationList.get(id);
    }

    public static String getLangName(String id) {
        if (localizationList.containsKey(id))
            return localizationList.get(id).getLocalizationName();
        return english.getLocalizationName();
    }

    public static String getLangID() {
        if (currentLang != null)
            return currentLang.getID();
        return "en_US";
    }

    public static String translate(String key, Object... objs) {
        String s = null;
        if (currentLang != null)
            s = currentLang.getLocalization(key);
        if (s == null && english != null)
            s = english.getLocalization(key);
        if (s == null)
            s = key;
        return String.format(s, objs);
    }

    public static List<String> getKeys() {
        List<String> keys = Lists.newArrayList();
        if (!localizationList.isEmpty()) {
            localizationList.keySet().stream().filter(key -> !Strings.isNullOrEmpty(key)).forEach(key -> keys.add(key));
        }
        return keys;
    }
}
