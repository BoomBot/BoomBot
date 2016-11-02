package net.lomeli.boombot.core.registry;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.data.EntityData;
import net.lomeli.boombot.api.data.GuildData;
import net.lomeli.boombot.api.data.IDataRegistry;
import net.lomeli.boombot.api.events.bot.data.DataEvent;
import net.lomeli.boombot.api.util.BasicGuildUtil;

public class DataRegistry implements IDataRegistry {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String BOOM_BOT_DATA = "boombot.cfg";
    private File dataFolder;
    private Map<String, GuildData> dataRegistry;
    private EntityData boomBotData;

    public DataRegistry(File dataFolder) {
        this.dataFolder = dataFolder;
        this.dataRegistry = Maps.newHashMap();
        this.boomBotData = new EntityData();
        if (dataFolder != null && !dataFolder.exists()) {
            BoomBot.logger.info("Creating data folder");
            dataFolder.mkdir();
        }
    }

    @Override
    public GuildData getDataForGuild(String id) {
        addGuild(id);
        return dataRegistry.get(id);
    }

    public EntityData getBoomBotData() {
        return boomBotData;
    }

    @Override
    public void readGuildData() {
        // read BoomBot data. Addons do NOT need to read this!
        File boomBotdata = new File(BOOM_BOT_DATA);
        if (boomBotdata.exists()) {
            try {
                String data = FileUtils.readFileToString(boomBotdata, "UTF-8");
                this.boomBotData = gson.fromJson(data, EntityData.class);
                if (this.boomBotData == null) this.boomBotData = new EntityData();
            } catch (IOException ex) {
                BoomBot.logger.error("Could not read boombot config!", ex);
            }
        } else {
            this.boomBotData.setString("OwnerID", "null");
            writeGuildData();
        }
        // Read guild data
        if (dataFolder != null && dataFolder.exists() && dataFolder.isDirectory()) {
            File[] dataFiles = dataFolder.listFiles((dir, name) -> FilenameUtils.isExtension(name, "cfg"));
            if (dataFiles != null && dataFiles.length > 0) {
                for (File file : dataFiles) {
                    try {
                        String id = FilenameUtils.getBaseName(file.getCanonicalPath());
                        GuildData data = new GuildData(id);
                        data.readData(dataFolder);
                        dataRegistry.put(id, data);
                    } catch (IOException ex) {
                        BoomBot.logger.error("Could not read file {}", file.getName());
                        ex.printStackTrace();
                    }
                }
            }
            BoomAPI.eventRegistry.post(new DataEvent.DataReadEvent(dataRegistry));
        }
    }

    @Override
    public void writeGuildData() {
        // Write BoomBot data. Addons should NOT be able to write to it.
        try {
            File boomBotdata = new File(BOOM_BOT_DATA);
            String data = gson.toJson(this.boomBotData);
            FileUtils.write(boomBotdata, data, "UTF-8");
        } catch (IOException ex) {
            BoomBot.logger.error("Failed to write BoomBot base data", ex);
        }
        // Write guild data
        DataEvent.DataWriteEvent event = new DataEvent.DataWriteEvent(dataRegistry);
        BoomAPI.eventRegistry.post(event);
        dataRegistry.putAll(event.getData());
        if (dataRegistry.size() > 0) {
            for (Map.Entry<String, GuildData> entry : dataRegistry.entrySet())
                entry.getValue().writeData(dataFolder);
        }
    }

    public void writeGuildData(String guildID) {
        if (guildHasData(guildID)) {
            DataEvent.DataWriteEvent event = new DataEvent.DataWriteEvent(dataRegistry);
            BoomAPI.eventRegistry.post(event);
            dataRegistry.putAll(event.getData());
            GuildData data = getDataForGuild(guildID);
            if (data != null) data.writeData(dataFolder);
        }
    }

    @Override
    public void addGuild(String id) {
        if (!guildHasData(id)) {
            GuildData data = new GuildData(id);
            BasicGuildUtil.setGuildLang(data, BasicGuildUtil.DEFAULT_LANG);
            BasicGuildUtil.setGuildCommandKey(data, BasicGuildUtil.DEFAULT_KEY);
            BasicGuildUtil.setGuildCommandDelay(data, 0);
            BasicGuildUtil.setGuildAllowBotMention(data, false);
            BasicGuildUtil.setGuildAllowBotTTS(data, false);
            BasicGuildUtil.setGuildAllowHereMention(data, false);
            BasicGuildUtil.setGuildAllowEveryoneMention(data, false);

            dataRegistry.put(id, data);
        }
    }

    @Override
    public boolean guildHasData(String id) {
        return dataRegistry.containsKey(id);
    }
}
