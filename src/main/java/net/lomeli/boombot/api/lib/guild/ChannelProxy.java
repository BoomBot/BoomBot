package net.lomeli.boombot.api.lib.guild;

public class ChannelProxy {
    private String channelID, channelName;
    private ChannelType type;

    public ChannelProxy(String channelID, String channelName, ChannelType type) {
        this.channelID = channelID;
        this.channelName = channelName;
        this.type = type;
    }

    public ChannelType getType() {
        return type;
    }

    public String getChannelID() {
        return channelID;
    }

    public String getChannelName() {
        return channelName;
    }

    public static enum ChannelType {
        TEXT, VOICE, UNKNOWN;
    }
}
