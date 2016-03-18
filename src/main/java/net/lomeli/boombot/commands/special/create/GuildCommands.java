package net.lomeli.boombot.commands.special.create;

import com.google.common.collect.Lists;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import java.util.Iterator;
import java.util.List;

import net.lomeli.boombot.commands.Command;

public class GuildCommands {
    private List<Command> commandList;
    private List<String> banUsers;
    private String guildID;

    public GuildCommands(String guild) {
        this.guildID = guild;
        this.commandList = Lists.newArrayList();
        this.banUsers = Lists.newArrayList();
    }

    public GuildCommands(Guild guild) {
        this(guild.getId());
    }

    public boolean addGuildCommand(Command command) {
        if (command == null) return false;
        for (Command c : commandList) {
            if (c != null && c.getName().equalsIgnoreCase(command.getName()))
                return false;
        }
        commandList.add(command);
        return true;
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
        if (user != null && !banUsers.contains(user.getId())) {
            banUsers.add(user.getId());
            return true;
        }
        return false;
    }

    public boolean removeBannedUser(User user) {
        if (user != null && banUsers.contains(user.getId())) {
            banUsers.remove(user.getId());
            return true;
        }
        return false;
    }

    public boolean isUserBanned(User user) {
        return user != null && banUsers.contains(user.getId());
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
}
