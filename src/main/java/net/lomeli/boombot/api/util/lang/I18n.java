package net.lomeli.boombot.api.util.lang;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import net.lomeli.boombot.api.BoomAPI;

public class I18n {
    private HashMap<String, String> translations;
    private String name;

    public I18n() {
        translations = Maps.newHashMap();
    }

    public void loadTranslations(File localizationFile) {
        try {
            name = FilenameUtils.getBaseName(localizationFile.getCanonicalPath());
            BufferedReader br = new BufferedReader(new FileReader(localizationFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty() && !line.startsWith("#") && line.contains("=")) {
                    String[] info = line.split("=");
                    if (info != null && info.length >= 2) {
                        String key = info[0];
                        String translate = "";
                        for (int i = 1; i < info.length; i++)
                            translate += (i > 1 ? "=" : "") + info[i];
                        translations.put(key, translate);
                    }
                }
            }
            br.close();
            if (translations.containsKey("langfile.langauage")) name = translations.get("langfile.langauage");
        } catch (IOException ex) {
            BoomAPI.logger.error("Could not load localization file {}", localizationFile.getName());
            ex.printStackTrace();
        }
    }

    public String getLocalizationName() {
        return name;
    }

    public String getLocalization(String key) {
        if (translations.containsKey(key))
            return translations.get(key);
        return key;
    }
}
