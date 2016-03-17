package net.lomeli.boombot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import net.lomeli.boombot.commands.CommandRegistry;
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
            InputStreamReader configReader = new InputStreamReader(new FileInputStream(mainFile));
            if (configReader != null) {
                BoomBot.config = gson.fromJson(configReader, BoomConfig.class);
                configReader.close();
            }
            BoomBot.config.customCommands.stream().filter(c -> c != null).forEach(c -> CommandRegistry.INSTANCE.addNewCommand(c));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeConfig() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String stuff = gson.toJson(BoomBot.config, BoomConfig.class);
            FileWriter writer = new FileWriter(mainFile);
            writer.write(stuff);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
