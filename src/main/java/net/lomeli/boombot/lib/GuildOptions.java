package net.lomeli.boombot.lib;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.commands.CommandRegistry;

public class GuildOptions {
    private List<Command> commandList;
    private List<String> banUsers;
    private List<String> restrictedChannels;
    private String guildID;
    private boolean announceReady;
    private boolean announceStopped;
    private int secondsDelay;
    private transient Map<String, Long> channelDelay;

    public GuildOptions(String guild) {
        this.guildID = guild;
        this.commandList = Lists.newArrayList();
        this.banUsers = Lists.newArrayList();
        this.restrictedChannels = Lists.newArrayList();
        this.announceReady = true;
        this.announceStopped = true;
        this.secondsDelay = 2;
        this.channelDelay = Maps.newHashMap();
    }

    public GuildOptions(Guild guild) {
        this(guild.getId());
    }

    public boolean addGuildCommand(Command command) {
        if (command == null) return false;
        for (Command c : CommandRegistry.INSTANCE.getCommands()) {
            if (c != null && c.getName().equalsIgnoreCase(command.getName()))
                return false;
        }
        for (Command c : commandList) {
            if (c != null && c.getName().equalsIgnoreCase(command.getName()))
                return false;
        }
        return commandList.add(command);
    }

    public boolean removeGuildCommand(String name) {
        Iterator<Command> it = commandList.listIterator();
        while (it.hasNext()) {
            Command c = it.next();
            if (c != null && c.getName().equalsIgnoreCase(name)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public boolean banCommandUser(User user) {
        if (user != null && !banUsers.contains(user.getId()))
            return banUsers.add(user.getId());
        return false;
    }

    public boolean removeBannedUser(User user) {
        return user != null ? banUsers.remove(user.getId()) : false;
    }

    public boolean restrictChannel(String channelID) {
        if (!Strings.isNullOrEmpty(channelID) && !restrictedChannels.contains(channelID))
            return restrictedChannels.add(channelID);
        return false;
    }

    public boolean restrictChannel(TextChannel channel) {
        return channel != null ? restrictChannel(channel.getId()) : false;
    }

    public boolean freeChannel(String channelID) {
        return !Strings.isNullOrEmpty(channelID) ? restrictedChannels.remove(channelID) : false;
    }

    public boolean freeChannel(TextChannel channel) {
        return channel != null ? freeChannel(channel.getId()) : false;
    }

    public boolean isChannelRestricted(String channelID) {
        return restrictedChannels.contains(channelID);
    }

    public boolean isChannelRestricted(TextChannel channel) {
        return channel != null ? isChannelRestricted(channel.getId()) : false;
    }

    public boolean isUserBanned(User user) {
        return user != null && banUsers.contains(user.getId());
    }

    public long getLastCommandUsedChannel(String id) {
        if (Strings.isNullOrEmpty(id) || !channelDelay.containsKey(id)) return 0;
        return channelDelay.get(id);
    }

    public long getLastCommandUsedChannel(TextChannel channel) {
        return channel != null ? getLastCommandUsedChannel(channel.getId()) : 0;
    }

    public void updateLastCommand(String id) {
        if (!Strings.isNullOrEmpty(id))
            channelDelay.put(id, System.currentTimeMillis());
    }

    public void updateLastCommand(TextChannel channel) {
        if (channel != null) updateLastCommand(channel.getId());
    }

    public void clearCommands() {
        commandList.clear();
    }

    public List<Command> getCommandList() {
        return commandList;
    }

    public String getGuildID() {
        return guildID;
    }

    public boolean announceReady() {
        return announceReady;
    }

    public boolean announceStopped() {
        return announceStopped;
    }

    public int getSecondsDelay() {
        return secondsDelay;
    }

    public Map<String, Long> getChannelDelay() {
        return channelDelay;
    }
}