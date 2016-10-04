package net.lomeli.boombot.command.custom;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.data.EntityData;
import net.lomeli.boombot.api.data.GuildData;
import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.events.bot.data.DataEvent;

public enum CustomCommandRegistry {
    INSTANCE();

    private static final String CUSTOM_DATA_KEY = "boombot_custom_command_data";

    Map<String, Map<String, CustomContent>> guildCommands;

    CustomCommandRegistry() {
        this.guildCommands = Maps.newHashMap();
        BoomAPI.eventRegistry.registerEventHandler(this);
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

    @Event.EventHandler
    public void readDataEvent(DataEvent.DataReadEvent event) {
        if (event.getGuildIDs() == null || event.getGuildIDs().length <= 0) return;
        for (String guildID : event.getGuildIDs()) {
            GuildData guildData = event.getGuildData(guildID);
            if (guildData != null && guildData.getGuildData() != null) {
                EntityData rawData = guildData.getGuildData();
                if (rawData != null && rawData.hasKey(CUSTOM_DATA_KEY)) {
                    EntityData commandData = rawData.getData(CUSTOM_DATA_KEY);
                    String[] commandNames = commandData.getKeys();
                    if (commandNames == null || commandNames.length <= 0) continue;
                    for (String name : commandNames) {
                        String command = commandData.getString(name);
                        if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(command))
                            addGuildCommand(guildID, new CustomContent(name, command));
                    }
                }
            }
        }
    }
}
