package net.lomeli.boombot.api.events.text;

import net.lomeli.boombot.api.events.Event;

@Event.Cancelable
public class MessageEvent extends Event {
    private final String userID, channelID, guildID, rawMessage, message;

    public MessageEvent(String userID, String channelID, String guildID, String rawMessage, String message) {
        this.userID = userID;
        this.channelID = channelID;
        this.guildID = guildID;
        this.rawMessage = rawMessage;
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public String getChannelID() {
        return channelID;
    }

    public String getGuildID() {
        return guildID;
    }

    public String getRawMessage() {
        return rawMessage;
    }

    public String getMessage() {
        return message;
    }
}