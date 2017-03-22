package net.lomeli.boombot.api.lib.guild;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import net.lomeli.boombot.api.lib.UserProxy;

public class GuildProxy {
    private List<ChannelProxy> channels;
    private List<UserProxy> users;
    private String ownerID, guildID, guildName, publicChannelID;

    public GuildProxy(String guildID, String ownerID, String guildName, String publicChannelID, List<ChannelProxy> channels, List<UserProxy> users) {
        this.guildID = guildID;
        this.ownerID = ownerID;
        this.guildName = guildName;
        this.publicChannelID = publicChannelID;
        this.channels = channels;
        if (this.channels == null) this.channels = Lists.newArrayList();
        this.users = users;
        if (this.users == null) this.users = Lists.newArrayList();
    }

    public String getGuildID() {
        return guildID;
    }

    public String getGuildName() {
        return guildName;
    }

    public String getPublicChannelID() {
        return publicChannelID;
    }

    public List<ChannelProxy> getChannels() {
        return Collections.unmodifiableList(channels);
    }

    public List<UserProxy> getUsers() {
        return Collections.unmodifiableList(users);
    }
}
