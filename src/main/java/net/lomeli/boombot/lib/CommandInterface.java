package net.lomeli.boombot.lib;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

import java.util.List;
import java.util.Random;

import net.lomeli.boombot.BoomBot;

public class CommandInterface {
    private GuildOptions guildOptions;
    private Guild guild;
    private User user;
    private TextChannel channel;
    private Message message;
    private String command;
    private List<String> args;
    public Random rand = new Random(System.currentTimeMillis());

    public CommandInterface(Message message, GuildOptions guildOptions, User user, TextChannel channel, String command, List<String> args) {
        this.message = message;
        this.user = user;
        this.channel = channel;
        this.command = command;
        this.args = args;
        this.guildOptions = guildOptions;
        this.guild = BoomBot.jda.getGuildById(guildOptions.getGuildID());
    }

    public TextChannel getChannel() {
        return channel;
    }

    public void sendMessage(String str, Object... args) {
        String content = getGuildOptions().translate(str, args);
        boolean tts = false;
        if (guildOptions.allowTTS() && content.startsWith("/tts"))
            tts = true;
        content = content.replace("/tts", "");
        getChannel().sendMessage(new MessageBuilder().setTTS(tts).appendString(content).build());
    }

    public Guild getGuild() {
        return guild;
    }

    public User getUser() {
        return user;
    }

    public void sendUserMessage(String str, Object... args) {
        user.getPrivateChannel().sendMessage(getGuildOptions().translate(str, args));
    }

    public List<String> getArgs() {
        return args;
    }

    public String getCommand() {
        return command;
    }

    public GuildOptions getGuildOptions() {
        guildOptions.initGuildOptions();
        return guildOptions;
    }

    public Message getMessage() {
        return message;
    }
}
