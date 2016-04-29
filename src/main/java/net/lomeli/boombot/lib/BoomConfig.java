package net.lomeli.boombot.lib;

import com.google.common.collect.Lists;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;

public class BoomConfig {
    private List<GuildOptions> guildOptions;
    private boolean updatable;

    public BoomConfig() {
        guildOptions = Lists.newArrayList();
        updatable = false;
    }

    public boolean addGuildCommand(GuildOptions guildOptions, Command command) {
        boolean flag = guildOptions.addGuildCommand(command);
        updateGuildCommand(guildOptions);
        return flag;
    }

    public boolean removeGuildCommand(GuildOptions guildOptions, String name) {
        boolean flag = guildOptions.removeGuildCommand(name);
        updateGuildCommand(guildOptions);
        return flag;
    }

    public void clearGuildCommands(GuildOptions guildOptions) {
        guildOptions.clearCommands();
        updateGuildCommand(guildOptions);
    }

    public boolean banUserCommands(GuildOptions guildOptions, User user) {
        boolean flag = guildOptions.banCommandUser(user);
        updateGuildCommand(guildOptions);
        return flag;
    }

    public boolean removeCommandBan(GuildOptions guildOptions, User user) {
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
        GuildOptions options = new GuildOptions(guild);
        this.guildOptions.add(options);
        return options;
    }

    public boolean isUpdatable() {
        return updatable;
    }
}
