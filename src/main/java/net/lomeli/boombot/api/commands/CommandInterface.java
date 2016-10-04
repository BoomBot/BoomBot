package net.lomeli.boombot.api.commands;

import com.google.common.collect.Lists;

import java.util.List;

public class CommandInterface {
    private String userID;
    private String guildID;
    private String channelID;
    private String message;
    private List<String> args;

    public CommandInterface(String userID, String guildID, String channelID, String message, String... args) {
        this.userID = userID;
        this.guildID = guildID;
        this.channelID = channelID;
        this.message = message;
        this.args = Lists.newArrayList();
        if (args != null && args.length > 0)
            for (String s : args) this.args.add(s);
    }

    public String getUserID() {
        return userID;
    }

    public String getGuildID() {
        return guildID;
    }

    public String getChannelID() {
        return channelID;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getArgs() {
        return args;
    }
}
