package net.lomeli.boombot.command.custom;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.events.bot.data.DataEvent;
import net.lomeli.boombot.api.nbt.TagBase;
import net.lomeli.boombot.api.nbt.TagCompound;

public enum CustomRegistry {
    INSTANCE();

    private static final String CUSTOM_DATA_KEY = "boombot_custom_command_data";

    Map<String, Map<String, CustomContent>> guildCommands;

    CustomRegistry() {
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
        if (commands != null && !Strings.isNullOrEmpty(commandID) && commands.containsKey(commandID.toLowerCase())) {
            commands.remove(commandID.toLowerCase());
            guildCommands.put(guildID, commands);
            flag = true;
        }
        return flag;
    }

    @Event.EventHandler
    public void readDataEvent(DataEvent.DataReadEvent event) {
        BoomBot.logger.debug("Reading custom commands");
        if (event.getGuildIDs() == null || event.getGuildIDs().length <= 0) return;
        Lists.newArrayList(event.getGuildIDs()).stream().filter(id -> !Strings.isNullOrEmpty(id))
                .forEach(id -> {
                    TagCompound guildData = event.getGuildData(id);
                    if (guildData != null && guildData.hasTag(CUSTOM_DATA_KEY, TagBase.TagType.TAG_COMPOUND)) {
                        BoomBot.logger.debug("Loading %s's custom commands!", guildData.getString("name"));
                        TagCompound commandData = guildData.getTagCompound(CUSTOM_DATA_KEY);
                        Collection<String> commandNames = commandData.getKeys();
                        commandNames.stream().filter(name -> commandData.hasTag(name, TagBase.TagType.TAG_STRING)).forEach(name -> {
                            String command = commandData.getString(name);
                            if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(command))
                                addGuildCommand(id, new CustomContent(name, command));
                        });
                    }
                });
    }

    @Event.EventHandler
    public void writeDataEvent(DataEvent.DataWriteEvent event) {
        BoomBot.logger.debug("Writing custom commands");
        guildCommands.entrySet().stream().forEach(entry -> {
            TagCompound guildData = event.getGuildData(entry.getKey());
            if (guildData == null) guildData = new TagCompound();
            TagCompound customCommands = new TagCompound();
            Map<String, CustomContent> commands = entry.getValue();
            if (commands != null && commands.size() > 0) {
                commands.values().stream().filter(command -> command != null)
                        .forEach(command -> customCommands.setString(command.getCommandName(), command.getCommandContent()));
            }
            guildData.setTag(CUSTOM_DATA_KEY, customCommands);
            event.getData().put(entry.getKey(), guildData);
        });
    }
}
