package net.lomeli.boombot.lib.stats;

import com.google.common.collect.Maps;
import net.dv8tion.jda.entities.User;

import java.util.HashMap;

public class UserCommandUsage {
    private String userID;
    private HashMap<String, Integer> commandUsage;

    public UserCommandUsage(String userID) {
        this.userID = userID;
        this.commandUsage = Maps.newHashMap();
    }

    public UserCommandUsage(User user) {
        this(user.getId());
    }

    public String getUserID() {
        return userID;
    }

    public HashMap<String, Integer> getCommandUsage() {
        return commandUsage;
    }

    public int getCommandUsage(String command) {
        return commandUsage.containsKey(command.toLowerCase()) ? commandUsage.get(command.toLowerCase()) : 0;
    }

    public void incrementUsage(String command) {
        int usage = 1;
        if (commandUsage.containsKey(command.toLowerCase()))
            usage += commandUsage.get(command.toLowerCase());
        commandUsage.put(command.toLowerCase(), usage);
    }

    public void removeCommand(String command) {
        if (commandUsage.containsKey(command.toLowerCase()))
            commandUsage.remove(command.toLowerCase());
    }
}
