package net.lomeli.boombot.api.commands;

import com.google.common.collect.Lists;

import java.util.List;

import net.lomeli.boombot.api.lib.UserProxy;
import net.lomeli.boombot.api.nbt.NBTTagCompound;
import net.lomeli.boombot.api.util.GuildUtil;

public class CommandData {
    private final String guildID, channelID, message;
    private UserProxy user;
    private List<String> args, mentionedUserIDs;
    private NBTTagCompound guildData, userData;

    public CommandData(UserProxy user, String guildID, String channelID, String message, List<String> mentionedUser, String... args) {
        this.user = user;
        this.guildID = guildID;
        this.channelID = channelID;
        this.message = message;
        this.args = Lists.newArrayList();
        if (args != null && args.length > 0) {
            this.args.addAll(Lists.newArrayList(args));
            this.args.remove(0);
        }
        this.mentionedUserIDs = Lists.newArrayList(mentionedUser);
        this.guildData = GuildUtil.getGuildData(guildID);
        this.userData = GuildUtil.getGuildMemberData(this.guildData, this.user.getUserID());
    }

    public UserProxy getUserInfo() {
        return user;
    }

    public String getGuildID() {
        return guildID;
    }

    public String getChannelID() {
        return channelID;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getArgs() {
        return args;
    }

    public List<String> getMentionedUserIDs() {
        return mentionedUserIDs;
    }

    public NBTTagCompound getGuildData() {
        return guildData;
    }

    public NBTTagCompound getUserData() {
        return userData;
    }
}
