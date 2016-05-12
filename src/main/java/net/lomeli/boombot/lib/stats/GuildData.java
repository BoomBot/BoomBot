package net.lomeli.boombot.lib.stats;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import net.lomeli.boombot.helper.UserHelper;

public class GuildData {
    private String guildName, ownerName, mainChannel;
    private int onlineUsers, awayUsers, offlineUsers, totalUsers, textChannels, voiceChannels;

    public GuildData(Guild guild) {
        onlineUsers = 0;
        awayUsers = 0;
        offlineUsers = 0;
        if (guild != null) {
            guildName = guild.getName();
            User owner = UserHelper.getUser(guild, guild.getOwnerId());
            if (owner != null)
                ownerName = owner.getUsername();
            if (guild.getPublicChannel() != null)
                mainChannel = guild.getPublicChannel().getName();
            textChannels = guild.getTextChannels().size();
            voiceChannels = guild.getVoiceChannels().size();
            for (User user : guild.getUsers()) {
                if (user != null) {
                    ++totalUsers;
                    switch (user.getOnlineStatus()) {
                        case ONLINE:
                            ++onlineUsers;
                            break;
                        case OFFLINE:
                            ++offlineUsers;
                            break;
                        case AWAY:
                            ++awayUsers;
                            break;
                    }
                }
            }
        }
    }

    public int getAwayUsers() {
        return awayUsers;
    }

    public int getOfflineUsers() {
        return offlineUsers;
    }

    public int getOnlineUsers() {
        return onlineUsers;
    }

    public int getTextChannels() {
        return textChannels;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public int getVoiceChannels() {
        return voiceChannels;
    }

    public String getMainChannel() {
        return mainChannel;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getGuildName() {
        return guildName;
    }
}
