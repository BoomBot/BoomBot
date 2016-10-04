package net.lomeli.boombot.command.custom;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

public enum CustomCommandRegistry {
    INSTANCE;

    Map<String, Map<String, CustomContent>> guildCommands;

    CustomCommandRegistry(){
        this.guildCommands = Maps.newHashMap();
    }

    public CustomContent getGuildCommand(String guildID, String commandID) {
        CustomContent command = null;
        if (guildCommands.containsKey(guildID)) {
            Map<String, CustomContent> commands = guildCommands.get(guildID);
            if (commands != null && commands.containsKey(commandID))
                command = commands.get(commandID);
        }
        return command;
    }

    /**
     * @return true if it can add the command to the guild's custom command list
     */
    public boolean addGuildCommand(String guildID, CustomContent commandContent) {
        boolean flag = false;
        Map<String, CustomContent> commands = guildCommands.get(guildID);
        if (commands == null) commands = Maps.newHashMap();
        if (commandContent != null && !Strings.isNullOrEmpty(commandContent.getCommandName()) &&
                !Strings.isNullOrEmpty(commandContent.getCommandContent()) && !commands.containsKey(commandContent.getCommandName())) {
            commands.put(commandContent.getCommandName(), commandContent);
            guildCommands.put(guildID, commands);
            flag = true;
        }
        return flag;
    }

    /**
     * @return true if it can remove the command from the guild's custom command list
     */
    public boolean removeGuildCommand(String guildID, String commandID) {
        boolean flag = false;
        Map<String, CustomContent> commands = guildCommands.get(guildID);
        if (commands != null && !Strings.isNullOrEmpty(commandID) && commands.containsKey(commandID)) {
            commands.remove(commandID);
            guildCommands.put(guildID, commands);
            flag = true;
        }
        return flag;
    }
}
