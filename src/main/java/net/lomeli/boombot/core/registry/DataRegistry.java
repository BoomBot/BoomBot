package net.lomeli.boombot.core.registry;

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
import net.lomeli.boombot.api.nbt.NBTTagBase;
import net.lomeli.boombot.api.nbt.NBTTagCompound;
import net.lomeli.boombot.api.nbt.NBTTagList;
import net.lomeli.boombot.api.nbt.NBTUtil;
import net.lomeli.boombot.api.registry.IDataRegistry;
import net.lomeli.boombot.api.util.GuildUtil;

public class DataRegistry implements IDataRegistry {
    private static final String BOOM_BOT_DATA = "boombot.dat";
    private File dataFolder;
    private Map<String, NBTTagCompound> dataRegistry;
    private NBTTagCompound boomBotData;

    public DataRegistry(File dataFolder) {
        this.dataFolder = dataFolder;
        this.dataRegistry = Maps.newHashMap();
        this.boomBotData = new NBTTagCompound();
        if (dataFolder != null && !dataFolder.exists()) {
            BoomBot.logger.info("Creating data folder");
            dataFolder.mkdir();
        }
    }

    @Override
    public NBTTagCompound getDataForGuild(String guildID) {
        addGuild(guildID);
        return dataRegistry.get(guildID);
    }

    @Override
    public NBTTagCompound getBoomBotData() {
        return boomBotData;
    }

    @Override
    public void readBoomBotData() {
        File boomBotdata = new File(BOOM_BOT_DATA);
        if (boomBotdata.exists()) {
            try {
                this.boomBotData = NBTUtil.readCompressed(new FileInputStream(boomBotdata));
            } catch (IOException ex) {
                BoomBot.logger.error("Could not read boombot config!", ex);
            }
        } else {
            this.boomBotData = new NBTTagCompound();
            this.boomBotData.setTag("adminIDs", new NBTTagList(NBTTagBase.TagType.TAG_STRING));
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
                            NBTTagCompound tag = NBTUtil.readCompressed(new FileInputStream(file));
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
            NBTUtil.writeCompressed(this.boomBotData, new FileOutputStream(boomBotdata));
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
            for (Map.Entry<String, NBTTagCompound> entry : dataRegistry.entrySet()) {
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
            NBTTagCompound data = getDataForGuild(guildID);
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
                NBTTagCompound data = new NBTTagCompound();
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
