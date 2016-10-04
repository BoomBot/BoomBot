package net.lomeli.boombot.lib;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.data.GuildData;
import net.lomeli.boombot.api.data.IDataRegistry;
import net.lomeli.boombot.api.events.bot.data.DataEvent;
import net.lomeli.boombot.api.util.BasicGuildUtil;

public class DataRegistry implements IDataRegistry {
    private File dataFolder;
    private Map<String, GuildData> dataRegistry;

    public DataRegistry(File dataFolder) {
        this.dataFolder = dataFolder;
        this.dataRegistry = Maps.newHashMap();
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

    @Override
    public void readGuildData() {
        if (dataRegistry.size() > 0 && dataFolder != null && dataFolder.exists() && dataFolder.isDirectory()) {
            File[] dataFiles = dataFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return FilenameUtils.isExtension(name, "cfg");
                }
            });
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
        DataEvent.DataWriteEvent event = new DataEvent.DataWriteEvent(dataRegistry);
        BoomAPI.eventRegistry.post(event);
        dataRegistry.putAll(event.getData());
        if (dataRegistry.size() > 0) {
            for (Map.Entry<String, GuildData> entry : dataRegistry.entrySet())
                entry.getValue().writeData(dataFolder);
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
            dataRegistry.put(id, data);
        }
    }

    @Override
    public boolean guildHasData(String id) {
        return dataRegistry.containsKey(id);
    }
}
