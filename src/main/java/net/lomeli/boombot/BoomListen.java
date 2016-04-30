package net.lomeli.boombot;

import com.google.common.collect.Lists;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.ShutdownEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.io.File;
import java.util.List;

import net.lomeli.boombot.commands.CommandRegistry;
import net.lomeli.boombot.helper.Logger;
import net.lomeli.boombot.helper.UserHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class BoomListen extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {

    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Logger.info("Joining %s...", event.getGuild().getName());
        if (BoomBot.config.getGuildOptions(event.getGuild()) == null) {
            BoomBot.config.updateGuildCommand(new GuildOptions(event.getGuild()));
        }
    }

    @Override
    public void onGenericGuildMessage(GenericGuildMessageEvent event) {
        if (BoomBot.jda == null || BoomBot.jda.getSelfInfo() == null || event == null || event.getMessage() == null || event.getAuthor() == null)
            return;
        Message message = event.getMessage();
        if (message.isEdited() || UserHelper.isUserBoomBot(event.getAuthor()) || event.getAuthor().isBot())
            return;
        GuildOptions options = BoomBot.config.getGuildOptions(event.getGuild());
        if (!ready(options, event.getChannel())) return;
        String content = message.getRawContent();
        String key = options.getCommandKey();
        if (content.startsWith(key)) {
            String[] arr = content.split(" ");
            if (arr.length >= 1) {
                String potentialCommand = arr[0].substring(key.length(), arr[0].length());
                List<String> args = Lists.newArrayList();
                for (int i = 1; i < arr.length; i++) {
                    args.add(arr[i]);
                }
                CommandInterface cmd = new CommandInterface(message, options, event.getAuthor(), event.getChannel(), potentialCommand, args);
                if (CommandRegistry.INSTANCE.executeCommand(cmd))
                    cmd.getGuildOptions().updateLastCommand(event.getChannel());
            }
        }
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        BoomBot.configLoader.writeConfig();
        Logger.writeLogFile(BoomBot.logFolder, BoomBot.logFile);
    }

    private boolean ready(GuildOptions options, TextChannel channel) {
        return ((System.currentTimeMillis() - options.getLastCommandUsedChannel(channel)) / 1000 % 60) >= options.getSecondsDelay();
    }
}
