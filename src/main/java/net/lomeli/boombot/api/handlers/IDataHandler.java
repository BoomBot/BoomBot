package net.lomeli.boombot.api.handlers;

import net.lomeli.boombot.api.nbt.TagCompound;

public interface IDataHandler {
    /**
     * @param guildID
     * @return Get data for guildID. If none exists,
     * create a basic template for guild data.
     */
    TagCompound getDataForGuild(String guildID);

    /**
     * Read all data, if any exists
     */
    void readData();

    /**
     * Write all data
     */
    void writeData();

    /**
     * @param guildID
     * @return true if guildID had no associated data
     * and was given some.
     */
    boolean addGuild(String guildID);

    /**
     * @param id
     * @return true if guildID has associated data
     */
    boolean guildHasData(String id);

    TagCompound getBoomBotData();

    void readBoomBotData();
}
