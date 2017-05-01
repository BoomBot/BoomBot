package net.lomeli.boombot.core.handler;

import com.google.common.collect.Maps;
import net.dv8tion.jda.core.entities.Guild;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.events.bot.data.DataEvent;
import net.lomeli.boombot.api.nbt.TagBase;
import net.lomeli.boombot.api.nbt.TagCompound;
import net.lomeli.boombot.api.nbt.TagList;
import net.lomeli.boombot.api.nbt.NBTUtil;
import net.lomeli.boombot.api.handlers.IDataHandler;
import net.lomeli.boombot.api.util.GuildUtil;
import net.lomeli.boombot.core.AutoSaveThread;
import net.lomeli.boombot.lib.DataKeys;

public class DataHandler implements IDataHandler {
    private static final String BOOM_BOT_DATA = "boombot.dat";
    private File dataFolder;
    private Map<String, TagCompound> dataRegistry;
    private TagCompound boomBotData;

    public DataHandler(File dataFolder) {
        this.dataFolder = dataFolder;
        this.dataRegistry = Maps.newHashMap();
        this.boomBotData = new TagCompound();
        if (dataFolder != null && !dataFolder.exists()) {
            BoomBot.logger.info("Creating data folder");
            dataFolder.mkdir();
        }
    }

    @Override
    public TagCompound getDataForGuild(String guildID) {
        addGuild(guildID);
        return dataRegistry.get(guildID);
    }

    @Override
    public TagCompound getBoomBotData() {
        return boomBotData;
    }

    @Override
    public void readBoomBotData() {
        File boomBotdata = new File(BOOM_BOT_DATA);
        if (boomBotdata.exists()) {
            try {
                this.boomBotData = NBTUtil.readUncompressed(boomBotdata);
            } catch (IOException ex) {
                BoomBot.logger.error("Could not read boombot config!", ex);
            }
        } else {
            this.boomBotData = new TagCompound();
            this.boomBotData.setTag(DataKeys.ADMIN_IDS, new TagList(TagBase.TagType.TAG_STRING));
            this.boomBotData.setLong(DataKeys.AUTO_SAVE_DELAY, 900000L);
            writeBoomBotData();
        }
    }

    @Override
    public void readData() {
        // read BoomBot data. Addons do NOT need to read this!
        readBoomBotData();
        // Read guild data
        if (dataFolder != null && dataFolder.exists() && dataFolder.isDirectory()) {
            File[] dataFiles = dataFolder.listFiles((dir, name) -> FilenameUtils.isExtension(name, "dat"));
            if (dataFiles != null && dataFiles.length > 0) {
                for (File file : dataFiles) {
                    try {
                        String id = FilenameUtils.getBaseName(file.getCanonicalPath());
                        Guild guild = BoomBot.jda.getGuildById(id);
                        if (guild != null) {
                            TagCompound tag = NBTUtil.readCompressed(new FileInputStream(file));
                            dataRegistry.put(id, tag);
                        } else file.delete();
                    } catch (IOException ex) {
                        BoomBot.logger.error("Could not read file %s", ex, file.getName());
                    }
                }
            }
        }
        BoomAPI.eventRegistry.post(new DataEvent.DataReadEvent(dataRegistry, boomBotData));
    }

    public void writeBoomBotData() {
        try {
            File boomBotdata = new File(BOOM_BOT_DATA);
            if (!this.boomBotData.hasTag(DataKeys.ADMIN_IDS, TagBase.TagType.TAG_LIST))
                this.boomBotData.setTag(DataKeys.ADMIN_IDS, new TagList(TagBase.TagType.TAG_STRING));
            if (!this.boomBotData.hasTag(DataKeys.AUTO_SAVE_DELAY, TagBase.TagType.TAG_LONG))
                this.boomBotData.setLong(DataKeys.AUTO_SAVE_DELAY, AutoSaveThread.SAVE_DELAY);
            NBTUtil.writeUncompressed(this.boomBotData, boomBotdata);
        } catch (IOException ex) {
            BoomBot.logger.error("Failed to write BoomBot base data", ex);
        }
    }

    @Override
    public void writeData() {
        DataEvent.DataWriteEvent event = new DataEvent.DataWriteEvent(dataRegistry, boomBotData);
        BoomAPI.eventRegistry.post(event);
        writeBoomBotData();
        dataRegistry.putAll(event.getData());
        if (dataRegistry.size() > 0) {
            for (Map.Entry<String, TagCompound> entry : dataRegistry.entrySet()) {
                try {
                    if (!dataFolder.exists() || !dataFolder.isDirectory()) dataFolder.mkdir();
                    File guildConfig = new File(dataFolder, entry.getKey() + ".dat");
                    NBTUtil.writeCompressed(entry.getValue(), new FileOutputStream(guildConfig));
                } catch (IOException ex) {
                    BoomBot.logger.error("Could not write data for Guild id %s", ex, entry.getKey());
                }
            }
        }
    }

    public void writeGuildData(String guildID) {
        if (guildHasData(guildID)) {
            DataEvent.DataWriteEvent event = new DataEvent.DataWriteEvent(dataRegistry, boomBotData);
            BoomAPI.eventRegistry.post(event);
            dataRegistry.putAll(event.getData());
            TagCompound data = getDataForGuild(guildID);
            if (data != null) {
                try {
                    if (!dataFolder.exists() || !dataFolder.isDirectory()) dataFolder.mkdir();
                    File guildConfig = new File(dataFolder, guildID + ".dat");
                    NBTUtil.writeCompressed(data, new FileOutputStream(guildConfig));
                } catch (IOException ex) {
                    BoomBot.logger.error("Could not write data for Guild id %s", ex, guildID);
                }
            }
        }
    }

    @Override
    public void addGuild(String guildID) {
        if (!guildHasData(guildID)) {
            Guild guild = BoomBot.jda.getGuildById(guildID);
            if (guild != null) {
                TagCompound data = new TagCompound();
                GuildUtil.setGuildOwner(data, guild.getOwner().getUser().getId());
                GuildUtil.setGuildLang(data, GuildUtil.DEFAULT_LANG);
                GuildUtil.setGuildCommandKey(data, GuildUtil.DEFAULT_KEY);
                GuildUtil.setGuildCommandDelay(data, 0);
                GuildUtil.setGuildAllowBotMention(data, false);
                GuildUtil.setGuildAllowBotTTS(data, false);
                GuildUtil.setGuildAllowHereMention(data, false);
                GuildUtil.setGuildAllowEveryoneMention(data, false);

                dataRegistry.put(guildID, data);
            }
        }
    }

    @Override
    public boolean guildHasData(String id) {
        return dataRegistry.containsKey(id);
    }
}
