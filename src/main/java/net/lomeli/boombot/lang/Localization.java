package net.lomeli.boombot.lang;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Copied over from another project of mine, to be implemented at a future date.
 */
public class Localization {
    private File localizationFile;
    private HashMap<String, String> translations;

    public Localization(File localizationFile) {
        this.localizationFile = localizationFile;
        translations = Maps.newHashMap();
    }

    public void parseFile() throws Exception {
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
    }

    public String getLocalizationName() {
        String name = getLocalization("langfile.langauage");
        return name != null ? name : FilenameUtils.getBaseName(localizationFile.getAbsolutePath());
    }

    public String getID() {
        return FilenameUtils.getBaseName(localizationFile.getAbsolutePath());
    }

    public String getLocalization(String key) {
        if (translations.containsKey(key))
            return translations.get(key);
        return null;
    }
}
