package net.lomeli.boombot.api.event.text;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

import net.lomeli.boombot.api.event.Event;
import net.lomeli.boombot.commands.Command;

public class CommandEvent extends Event {
    public final Command command;
    public final User user;
    public final Guild guild;
    public final TextChannel channel;

    private CommandEvent(Command command, User user, Guild guild, TextChannel channel) {
        this.command = command;
        this.user = user;
        this.guild = guild;
        this.channel = channel;
    }

    public static class Pre extends CommandEvent {
        public Pre(Command command, User user, Guild guild, TextChannel channel) {
            super(command, user, guild, channel);
        }

        @Override
        public boolean cancelable() {
            return true;
        }
    }

    public static class Post extends CommandEvent {
        public Post(Command command, User user, Guild guild, TextChannel channel) {
            super(command, user, guild, channel);
        }
    }
}
