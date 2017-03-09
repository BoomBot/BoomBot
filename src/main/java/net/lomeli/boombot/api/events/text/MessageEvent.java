package net.lomeli.boombot.api.events.text;

import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.lib.UserProxy;

/**
 * Fires whenever a user sends a message.
 */
@Event.Cancelable
public class MessageEvent extends Event {
    private final UserProxy user;
    private final String channelID, guildID, rawMessage, message;

    public MessageEvent(UserProxy user, String channelID, String guildID, String rawMessage, String message) {
        this.user = user;
        this.channelID = channelID;
        this.guildID = guildID;
        this.rawMessage = rawMessage;
        this.message = message;
    }

    public UserProxy getUserInfo() {
        return user;
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