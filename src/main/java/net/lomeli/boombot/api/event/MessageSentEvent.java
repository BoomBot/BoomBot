package net.lomeli.boombot.api.event;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

public class MessageSentEvent extends Event {
    public final User user;
    public final Guild guild;
    public final TextChannel channel;
    public final Message message;

    public MessageSentEvent(User user, Guild guild, TextChannel channel, Message message) {
        this.user = user;
        this.guild = guild;
        this.channel = channel;
        this.message = message;
    }

    @Override
    public boolean cancelable() {
        return true;
    }
}
