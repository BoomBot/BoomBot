package net.lomeli.boombot.api.lib;

public class BotMessage {
    private String out, userID, guildID, channelID;
    private boolean privateMessage;

    public BotMessage(String guildID, String channelID, String out) {
        this.guildID = guildID;
        this.channelID = channelID;
        this.out = out;
    }

    public BotMessage(String guildID, String userID, String out, boolean privateMessage) {
        this.guildID = guildID;
        this.userID = userID;
        this.out = out;
        this.privateMessage = privateMessage;
    }

    public boolean isPrivateMessage() {
        return privateMessage;
    }

    public String getGuildID() {
        return guildID;
    }

    public String getChannelID() {
        return channelID;
    }

    public String getUserID() {
        return userID;
    }

    public String getOut() {
        return out;
    }
}
