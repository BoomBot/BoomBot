package net.lomeli.boombot.api.data;

public interface IDataRegistry {
    GuildData getDataForGuild(String id);

    void readGuildData();

    void writeGuildData();

    void addGuild(String id);

    boolean guildHasData(String id);
}
