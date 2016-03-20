package net.lomeli.boombot.lib;

import com.google.common.collect.Lists;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;

public class BoomConfig {
    public List<GuildOptions> guildOptions;

    public BoomConfig() {
        guildOptions = Lists.newArrayList();
    }

    public boolean addGuildCommand(Guild guild, Command command) {
        GuildOptions guildOptions = getGuildOptions(guild);
        boolean flag = guildOptions.addGuildCommand(command);
        updateGuildCommand(guildOptions);
        return flag;
    }

    public boolean removeGuildCommand(Guild guild, String name) {
        GuildOptions guildOptions = getGuildOptions(guild);
        boolean flag = guildOptions.removeGuildCommand(name);
        updateGuildCommand(guildOptions);
        return flag;
    }

    public void clearGuildCommands(Guild guild) {
        GuildOptions guildOptions = getGuildOptions(guild);
        guildOptions.clearCommands();
        updateGuildCommand(guildOptions);
    }

    public boolean banUserCommands(Guild guild, User user) {
        GuildOptions guildOptions = getGuildOptions(guild);
        boolean flag = guildOptions.banCommandUser(user);
        updateGuildCommand(guildOptions);
        return flag;
    }

    public boolean removeCommandBan(Guild guild, User user) {
        GuildOptions guildOptions = getGuildOptions(guild);
        boolean flag = guildOptions.removeBannedUser(user);
        updateGuildCommand(guildOptions);
        return flag;
    }

    public void updateGuildCommand(GuildOptions guildCommand) {
        int index = -1;
        for (int i = 0; i < this.guildOptions.size(); i++) {
            GuildOptions c = this.guildOptions.get(i);
            if (c != null && c.getGuildID().equalsIgnoreCase(guildCommand.getGuildID())) {
                index = i;
                break;
            }
        }
        if (index > -1 && index < this.guildOptions.size())
            this.guildOptions.set(index, guildCommand);
        else
            this.guildOptions.add(guildCommand);
        BoomBot.configLoader.writeConfig();
    }

    public GuildOptions getGuildOptions(Guild guild) {
        for (GuildOptions commandList : guildOptions) {
            if (commandList != null && commandList.getGuildID().equalsIgnoreCase(guild.getId()))
                return commandList;
        }
        return new GuildOptions(guild);
    }

    public List<Command> getCommandsForGuild(Guild guild) {
        return getGuildOptions(guild).getCommandList();
    }
}
