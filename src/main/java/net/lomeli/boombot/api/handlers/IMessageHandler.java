package net.lomeli.boombot.api.handlers;

import net.lomeli.boombot.api.lib.BotMessage;
import net.lomeli.boombot.api.lib.guild.GuildProxy;

public interface IMessageHandler {
    /**
     * Queue up a message to a channel or a user
     */
    void sendMessage(BotMessage msg);

    /**
     * Get information about a particular guild, such as
     * text and voice channels, users, and the owner's ID.
     */
    GuildProxy getGuildProxy(String guildID);
}
