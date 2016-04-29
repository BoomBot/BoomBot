package net.lomeli.boombot.commands;

import com.google.common.collect.Lists;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageEmbed;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.MessageEmbedImpl;
import net.dv8tion.jda.entities.impl.MessageImpl;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lomeli.boombot.helper.ChannelHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class Command {
    private static final Pattern MENTION_PATTER = Pattern.compile("<@((\\d){1,32})>");
    private String name;
    private String content;

    public Command(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public void executeCommand(CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        String fullContent = getContent().replaceAll("%n", "\n");
        if (!options.allowMentions()) fullContent = stripMentions(fullContent, cmd.getGuild());
        String user = options.allowMentions() ? ("<@" + cmd.getUser().getId() + ">") : cmd.getUser().getUsername();
        String data = options.translate(fullContent.replaceAll("%u", user).replaceAll("%U", user), cmd.getArgs());
        cmd.sendMessage(data);
    }

    protected String stripMentions(String raw, Guild guild) {
        String out = raw;
        Matcher matcher = MENTION_PATTER.matcher(raw);
        List<User> userList = guild.getUsers();
        while (matcher.find()) {
            String group = matcher.group();
            String id = group.substring(2, group.length() - 1);
            User user = null;
            userLoop:
            for (User u : userList) {
                if (u != null && u.getId().equalsIgnoreCase(id)) {
                    user = u;
                    break userLoop;
                }
            }
            if (user != null)
                out = out.replaceAll(group, "@" + user.getUsername());
        }
        return out;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public boolean canUserExecute(CommandInterface cmd) {
        return true;
    }

    public boolean canBoomBotExecute(CommandInterface cmd) {
        return true;
    }

    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        return "";
    }


    public enum UserType {
        USER(false), BOOMBOT(true);

        private final boolean boomBot;

        UserType(boolean boomBot) {
            this.boomBot = boomBot;
        }

        public boolean isBoomBot() {
            return boomBot;
        }
    }
}
