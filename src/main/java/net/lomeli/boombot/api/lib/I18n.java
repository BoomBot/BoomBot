package net.lomeli.boombot.api.lib;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.io.*;
import java.util.HashMap;

import net.lomeli.boombot.api.BoomAPI;

public class I18n {
    private HashMap<String, String> translations;
    private String id;
    private String name;

    public I18n(String id) {
        translations = Maps.newHashMap();
        this.id = id;
    }

    public void readBuffer(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.isEmpty() && !line.startsWith("#") && line.contains("=")) {
                String[] info = line.split("=");
                if (info != null && info.length >= 2) {
                    String key = info[0];
                    String translate = "";
                    for (int i = 1; i < info.length; i++)
                        translate += (i > 1 ? "=" : "") + info[i];
                    if (!translations.containsKey(key)) translations.put(key, translate);
                }
            }
        }
        br.close();
        if (Strings.isNullOrEmpty(name) && translations.containsKey("langfile.langauage"))
            name = translations.get("langfile.langauage");
    }

    public void loadTranslations(File localizationFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(localizationFile));
            readBuffer(br);
        } catch (IOException ex) {
            BoomAPI.logger.error("Could not load localization file %s", ex, localizationFile.getName());
        }
    }

    public void loadTranslations(InputStream stream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            readBuffer(br);
        } catch (IOException ex) {
            BoomAPI.logger.error("Could not read localization from stream!", ex);
        }
    }

    public String getLocalizationName() {
        return name;
    }

    public String getLocalizationID() {
        return id;
    }

    public String getLocalization(String key) {
        return translations.containsKey(key) ? translations.get(key) : key;
    }

    public String getLocalization(String key, Object... args) {
        String translation = getLocalization(key);
        return String.format(translation, args);
    }
}
