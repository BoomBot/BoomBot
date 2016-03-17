package net.lomeli.boombot.lib;

import com.google.common.collect.Lists;
import net.dv8tion.jda.entities.Guild;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.commands.special.create.GuildCommands;

public class BoomConfig {
    public List<GuildCommands> guildCommands;
    public int secondsDelay;

    public BoomConfig() {
        secondsDelay = 2;
        guildCommands = Lists.newArrayList();
    }

    public boolean addGuildCommand(Guild guild, Command command) {
        boolean flag = false;
        GuildCommands guildCommands = getGuildCommands(guild);
        if (guildCommands.addGuildCommand(command)) {
            flag = true;
        }
        updateGuildCommand(guildCommands);
        return flag;
    }

    public boolean removeGuildCommand(Guild guild, String name) {
        boolean flag = false;
        GuildCommands guildCommands = getGuildCommands(guild);
        if (guildCommands.removeGuildCommand(name))
            flag = true;
        updateGuildCommand(guildCommands);
        return flag;
    }

    public void clearGuildCommands(Guild guild) {
        GuildCommands guildCommands = getGuildCommands(guild);
        guildCommands.clearCommands();
        updateGuildCommand(guildCommands);
    }

    public void updateGuildCommand(GuildCommands guildCommand) {
        int index = -1;
        for (int i = 0; i < this.guildCommands.size(); i++) {
            GuildCommands c = this.guildCommands.get(i);
            if (c != null && c.getGuildID().equalsIgnoreCase(guildCommand.getGuildID())) {
                index = i;
                break;
            }
        }
        if (index > -1 && index < this.guildCommands.size())
            this.guildCommands.set(index, guildCommand);
        else
            this.guildCommands.add(guildCommand);
        BoomBot.configLoader.writeConfig();
    }

    public GuildCommands getGuildCommands(Guild guild) {
        for (GuildCommands commandList : guildCommands) {
            if (commandList != null && commandList.getGuildID().equalsIgnoreCase(guild.getId()))
                return commandList;
        }
        return new GuildCommands(guild);
    }

    public List<Command> getCommandsForGuild(Guild guild) {
        return getGuildCommands(guild).getCommandList();
    }
}
