package net.lomeli.boombot;

import com.google.common.collect.Lists;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.List;

import net.lomeli.boombot.commands.CommandRegistry;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class BoomListen extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        event.getJDA().getGuilds().stream().filter(guild -> guild != null).forEach(guild -> {
            GuildOptions options = BoomBot.config.getGuildOptions(guild);
            if (options != null && options.announceReady())
                guild.getPublicChannel().sendMessage("BoomBot is ready!");
        });
    }

    @Override
    public void onGenericGuildMessage(GenericGuildMessageEvent event) {
        if (BoomBot.jda == null || BoomBot.jda.getSelfInfo() == null || event == null || event.getMessage() == null || event.getAuthor() == null)
            return;
        if (!ready(event.getGuild(), event.getChannel())) return;
        Message message = event.getMessage();
        if (message.isEdited() || event.getAuthor().getUsername().equals(BoomBot.jda.getSelfInfo().getUsername()))
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
                if (CommandRegistry.INSTANCE.executeCommand(cmd))
                    cmd.getGuildOptions().updateLastCommand(event.getChannel());
            }
        }
    }

    private boolean ready(Guild guild, TextChannel channel) {
        GuildOptions options = BoomBot.config.getGuildOptions(guild);
        return ((System.currentTimeMillis() - options.getLastCommandUsedChannel(channel)) / 1000 % 60) >= options.getSecondsDelay();
    }
}
