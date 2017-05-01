package net.lomeli.boombot.api.handlers;

import net.lomeli.boombot.api.nbt.TagCompound;


public interface IDataHandler {
    TagCompound getDataForGuild(String guildID);

    void readData();

    void writeData();

    void addGuild(String guildID);

    boolean guildHasData(String id);

    TagCompound getBoomBotData();

    void readBoomBotData();
}
