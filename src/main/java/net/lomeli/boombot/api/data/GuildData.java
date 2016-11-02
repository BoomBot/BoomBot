package net.lomeli.boombot.api.data;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import net.lomeli.boombot.api.BoomAPI;

/**
 * Data attached to a guild. Holds data for the guild
 * itself, as well as any users and text/voice channels
 * on said guild
 */
public class GuildData {
    private EntityData guildData;
    private transient String guildID;
    private Map<String, EntityData> userData;
    private Map<String, EntityData> textData;
    private Map<String, EntityData> voiceData;

    public GuildData(String guildID) {
        this.guildID = guildID;
        this.guildData = new EntityData();
        this.userData = Maps.newHashMap();
        this.textData = Maps.newHashMap();
        this.voiceData = Maps.newHashMap();
    }

    public EntityData getGuildData() {
        return guildData;
    }

    public EntityData getUserData(String id) {
        if (!userData.containsKey(id)) userData.put(id, new EntityData());
        return userData.get(id);
    }

    public EntityData getTextChannelData(String id) {
        if (!textData.containsKey(id)) textData.put(id, new EntityData());
        return textData.get(id);
    }

    public EntityData getVoiceChannelData(String id) {
        if (!voiceData.containsKey(id)) voiceData.put(id, new EntityData());
        return voiceData.get(id);
    }

    public void readData(File dataFolder) {
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
            return;
        }
        BoomAPI.logger.debug("Reading data for guild id %s", guildID);
        Gson gson = new Gson();
        // Read generic guild data
        File dataFile = new File(dataFolder, guildID + ".cfg");
        if (dataFile.exists()) {
            try {
                guildData = gson.fromJson(new FileReader(dataFile), EntityData.class);
            } catch (FileNotFoundException ex) {
                BoomAPI.logger.error("Somehow couldn't find the config file for %s", guildID);
                ex.printStackTrace();
            }
        }
        File guildFolder = new File(dataFile, guildID);
        if (!guildFolder.exists()) {
            guildFolder.mkdir();
            return;
        }

        // Read user data
        File userFolder = new File(guildFolder, "userData");
        readMapData(userFolder, userData, gson);

        // Read Text Channel data
        File textFolder = new File(guildFolder, "textData");
        readMapData(textFolder, textData, gson);

        // Read Voice Channel data
        File voiceFolder = new File(guildFolder, "voiceData");
        readMapData(voiceFolder, voiceData, gson);
        BoomAPI.logger.info("Finished reading data for guild %s", guildData.getString("name"));
    }

    private void readMapData(File parentFolder, Map<String, EntityData> data, Gson gson) {
        if (parentFolder.exists()) {
            File[] files = parentFolder.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    if (f != null && f.isFile()) {
                        try {
                            String id = FilenameUtils.getBaseName(f.getCanonicalPath());
                            EntityData entityData = gson.fromJson(new FileReader(f), EntityData.class);
                            if (entityData != null && !Strings.isNullOrEmpty(id) && !data.containsKey(id))
                                data.put(id, entityData);
                        } catch (IOException ex) {
                            BoomAPI.logger.error("Failed to read data from file %s in %s", f.getName(), parentFolder.getName());
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } else parentFolder.mkdir();
    }

    public void writeData(File dataFolder) {
        if (!dataFolder.exists()) dataFolder.mkdir();
        BoomAPI.logger.info("Writing data for guild id %s", guildID);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File dataFile = new File(dataFolder, guildID + ".cfg");
        try {
            FileUtils.write(dataFile, gson.toJson(guildData), "UTF-8");
        } catch (IOException ex) {
            BoomAPI.logger.error("Failed to write guild data for " + this.guildID);
        }
        File guildFolder = new File(dataFile, guildID);
        if (!guildFolder.exists()) guildFolder.mkdir();

        // Write user data
        File userFolder = new File(guildFolder, "userData");
        writeMapData(userFolder, userData, gson);

        File textFolder = new File(guildFolder, "textData");
        writeMapData(textFolder, textData, gson);

        File voiceFolder = new File(guildFolder, "voiceData");
        writeMapData(voiceFolder, voiceData, gson);

        BoomAPI.logger.info("Finished writing data for guild %s", guildData.getString("name"));
    }

    private void writeMapData(File parentFolder, Map<String, EntityData> data, Gson gson) {
        if (!parentFolder.exists()) parentFolder.mkdir();
        if (data != null && data.size() > 0) {
            for (Map.Entry<String, EntityData> entry : data.entrySet()) {
                String id = entry.getKey();
                EntityData entityData = entry.getValue();
                if (!Strings.isNullOrEmpty(id) && entityData != null) {
                    try {
                        FileUtils.write(new File(parentFolder, id + ".cfg"), gson.toJson(entityData), "UTF-8");
                    } catch (IOException ex) {
                        BoomAPI.logger.error("Failed to write data for %s in %s", id, parentFolder.getName());
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
