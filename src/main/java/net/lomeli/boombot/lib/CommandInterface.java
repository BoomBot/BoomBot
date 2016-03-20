package net.lomeli.boombot.lib;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

import java.util.List;
import java.util.Objects;

import net.lomeli.boombot.BoomBot;

public class CommandInterface {
    private GuildOptions guildOptions;
    private Guild guild;
    private User user;
    private String channelID, command;
    private List<String> args;

    public CommandInterface(Guild guild, User user, String channelID, String command, List<String> args) {
        this.guild = guild;
        this.user = user;
        this.channelID = channelID;
        this.command = command;
        this.args = args;
        this.guildOptions = BoomBot.config.getGuildOptions(guild);
    }

    public TextChannel getChannel() {
        TextChannel channel = null;
        for (TextChannel text : getGuild().getTextChannels()) {
            if (text != null && text.getId().equals(getChannelID())) {
                channel = text;
                break;
            }
        }
        return channel;
    }

    public void sendMessage(String str, Object... args) {
        getChannel().sendMessage(String.format(str, args));
    }

    public Guild getGuild() {
        return guild;
    }

    public User getUser() {
        return user;
    }

    public void sendUserMessage(String str, Object... args) {
        user.getPrivateChannel().sendMessage(String.format(str, args));
    }

    public List<String> getArgs() {
        return args;
    }

    public String getChannelID() {
        return channelID;
    }

    public String getCommand() {
        return command;
    }

    public GuildOptions getGuildOptions() {
        return guildOptions;
    }
}
