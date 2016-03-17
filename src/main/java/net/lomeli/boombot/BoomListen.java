package net.lomeli.boombot;

import com.google.common.collect.Lists;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.List;

import net.lomeli.boombot.commands.CommandRegistry;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.Logger;

public class BoomListen extends ListenerAdapter {
    public long lastCommandTime;

    @Override
    public void onReady(ReadyEvent event) {
    }

    @Override
    public void onGenericGuildMessage(GenericGuildMessageEvent event) {
        if (!ready()) return;
        if (BoomBot.jda == null || BoomBot.jda.getSelfInfo() == null || event == null || event.getMessage() == null || event.getAuthor() == null)
            return;
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
                    lastCommandTime = System.currentTimeMillis();
                }
            }
        }
    }

    private boolean ready() {
        return ((System.currentTimeMillis() - lastCommandTime) / 1000 % 60) >= BoomBot.config.secondsDelay;
    }
}
