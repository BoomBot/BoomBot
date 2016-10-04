package net.lomeli.boombot.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.commands.Command;
import net.lomeli.boombot.api.commands.ICommandRegistry;

public class CommandRegistry implements ICommandRegistry {
    private final String BOOMBOT_ID = "boombot";
    private Map<String, List<Command>> commandList;
    private Map<String, List<String>> commandNameList;

    public CommandRegistry() {
        commandList = Maps.newHashMap();
        commandNameList = Maps.newHashMap();
    }

    private void initBuiltInCommands() {

    }

    @Override
    public boolean addCommand(Object addonInstance, Command command) {
        if (addonInstance != null && command != null) {
            Addon addonInfo = addonInstance.getClass().getAnnotation(Addon.class);
            if (addonInfo != null && !addonInfo.addonID().equalsIgnoreCase(BOOMBOT_ID)) {
                String id = addonInfo.addonID();
                List<Command> commands = null;
                List<String> names = null;
                if (commandList.containsKey(id)) commands = commandList.get(id);
                if (commandNameList.containsKey(id)) names = commandNameList.get(id);
                if (names == null) names = Lists.newArrayList();
                if (commands == null) commands = Lists.newArrayList();
                if (!names.contains(command.getName().toLowerCase())) {
                    commands.add(command);
                    names.add(command.getName().toLowerCase());
                    commandList.put(id, commands);
                    commandNameList.put(id, names);
                }
            }
        }
        return false;
    }

    @Override
    public Command getCommand(String name) {
        return null;
    }

    @Override
    public List<String> getAddonCommands(String addonID) {
        List<String> list = commandNameList.get(addonID);
        if (list == null) list = Lists.newArrayList();
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<String> getCommands() {
        return null;
    }
}
