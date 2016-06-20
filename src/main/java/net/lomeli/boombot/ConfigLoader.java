package net.lomeli.boombot;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import net.lomeli.boombot.helper.Logger;
import net.lomeli.boombot.lib.BoomConfig;

public class ConfigLoader {
    private File mainFile;

    public ConfigLoader(File mainFile) {
        this.mainFile = mainFile;
    }

    public void parseConfig() {
        if (mainFile == null)
            return;
        if (!mainFile.exists())
            writeConfig();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String data = FileUtils.readFileToString(mainFile, "UTF-8");
            if (!Strings.isNullOrEmpty(data))
                BoomBot.config = gson.fromJson(data, BoomConfig.class);
        } catch (Exception e) {
            Logger.error("Failed to read config file %s!", e, mainFile.toString());
        }
    }

    public void writeConfig() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String stuff = gson.toJson(BoomBot.config, BoomConfig.class);
            FileUtils.write(mainFile, stuff, "UTF-8");
        } catch (Exception e) {
            Logger.error("Could not write to %s!", e, mainFile.toString());
        }
    }
}
