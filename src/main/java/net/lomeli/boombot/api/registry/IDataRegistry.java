package net.lomeli.boombot.api.registry;

import net.lomeli.boombot.api.nbt.NBTTagCompound;


public interface IDataRegistry {
    NBTTagCompound getDataForGuild(String guildID);

    void readData();

    void writeData();

    void addGuild(String guildID);

    boolean guildHasData(String id);

    NBTTagCompound getBoomBotData();

    void readBoomBotData();
}
