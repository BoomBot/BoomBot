package net.lomeli.boombot;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.List;

import net.lomeli.boombot.commands.CommandRegistry;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class BoomListen extends ListenerAdapter {
    private HashMap<String, Long> guildTimers;

    public BoomListen() {
        guildTimers = Maps.newHashMap();
    }

    @Override
    public void onReady(ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            if (guild != null) {
                GuildOptions options = BoomBot.config.getGuildOptions(guild);
                if (options != null && options.announceReady())
                    guild.getPublicChannel().sendMessage("BoomBot is ready!");
            }
        }
    }

    @Override
    public void onGenericGuildMessage(GenericGuildMessageEvent event) {
        if (BoomBot.jda == null || BoomBot.jda.getSelfInfo() == null || event == null || event.getMessage() == null || event.getAuthor() == null)
            return;
        if (!ready(event.getGuild().getId())) return;
        Message message = event.getMessage();
        if (event.getAuthor().getUsername().equals(BoomBot.jda.getSelfInfo().getUsername()))
            return;
        String content = message.getContent();
        if (content.startsWith("!")) {
            String[] arr = content.split(" ");
            if (arr.length >= 1) {
                String potentialCommand = arr[0].substring(1, arr[0].length());
                List<String> args = Lists.newArrayList();
                for (int i = 1; i < arr.length; i++) {
                    args.add(arr[i]);
                }
                CommandInterface cmd = new CommandInterface(event.getGuild(), event.getAuthor(), message.getChannelId(), potentialCommand, args);
                if (CommandRegistry.INSTANCE.executeCommand(cmd)) {
                    guildTimers.put(event.getGuild().getId(), System.currentTimeMillis());
                }
            }
        }
    }

    private boolean ready(String id) {
        if (!guildTimers.containsKey(id))
            return true;
        return ((System.currentTimeMillis() - guildTimers.get(id)) / 1000 % 60) >= BoomBot.config.secondsDelay;
    }
}
