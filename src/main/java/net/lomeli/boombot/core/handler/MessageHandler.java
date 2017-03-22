package net.lomeli.boombot.core.handler;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.handlers.IMessageHandler;
import net.lomeli.boombot.api.lib.BotMessage;
import net.lomeli.boombot.api.lib.UserProxy;
import net.lomeli.boombot.api.lib.guild.ChannelProxy;
import net.lomeli.boombot.api.lib.guild.GuildProxy;

public class MessageHandler implements IMessageHandler {
    @Override
    public void sendMessage(BotMessage msg) {
        if (Strings.isNullOrEmpty(msg.getOut())) return;
        Guild guild = BoomBot.jda.getGuildById(msg.getGuildID());
        if (guild == null) return;
        if (msg.isPrivateMessage()) {
            Member member = guild.getMemberById(msg.getUserID());
            if (member != null) {
                if (!member.getUser().hasPrivateChannel()) member.getUser().openPrivateChannel().queue();
                member.getUser().getPrivateChannel().sendMessage(msg.getOut()).queue();
            }
        } else {
            TextChannel channel = guild.getTextChannelById(msg.getChannelID());
            if (channel != null) channel.sendMessage(msg.getOut()).queue();
        }
    }

    @Override
    public GuildProxy getGuildProxy(String guildID) {
        Guild guild = BoomBot.jda.getGuildById(guildID);
        if (guild == null) return null;
        List<ChannelProxy> channels = Lists.newArrayList();
        guild.getTextChannels().stream()
                .forEach(channel -> channels.add(new ChannelProxy(channel.getId(), channel.getName(), ChannelProxy.ChannelType.TEXT)));
        guild.getVoiceChannels().stream()
                .forEach(channel -> channels.add(new ChannelProxy(channel.getId(), channel.getName(), ChannelProxy.ChannelType.VOICE)));
        List<UserProxy> users = Lists.newArrayList();
        guild.getMembers().stream()
                .forEach(member -> users.add(new UserProxy(member.getUser().getId(), member.getUser().getName(), member.getNickname())));
        return new GuildProxy(guildID, guild.getOwner().getUser().getId(), guild.getName(), guild.getPublicChannel().getId(), channels, users);
    }
}
