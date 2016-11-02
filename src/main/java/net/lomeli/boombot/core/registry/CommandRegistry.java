package net.lomeli.boombot.core.registry;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.commands.ICommandRegistry;
import net.lomeli.boombot.command.admin.ShutdownCommand;
import net.lomeli.boombot.command.custom.MakeCommand;
import net.lomeli.boombot.command.test.TestCommand;

public class CommandRegistry implements ICommandRegistry {
    private final String BOOMBOT_ID = "boombot";
    private Map<String, List<ICommand>> commandList;
    private Map<String, List<String>> commandNameList;

    public CommandRegistry() {
        commandList = Maps.newHashMap();
        commandNameList = Maps.newHashMap();
        initBuiltInCommands();
    }

    private void initBuiltInCommands() {
        addBaseCommand(new TestCommand());
        addBaseCommand(new MakeCommand());
        addBaseCommand(new ShutdownCommand());
    }

    private boolean addBaseCommand(ICommand command) {
        return addCommand(command, BOOMBOT_ID);
    }

    private boolean addCommand(ICommand command, String id) {
        if (command == null) return false;
        List<ICommand> commands = null;
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
            return true;
        }
        return false;
    }

    @Override
    public boolean addCommand(Object addonInstance, ICommand command) {
        if (addonInstance != null && command != null) {
            Addon addonInfo = addonInstance.getClass().getAnnotation(Addon.class);
            if (addonInfo != null && !addonInfo.addonID().equalsIgnoreCase(BOOMBOT_ID)) {
                String id = addonInfo.addonID();
                addCommand(command, id);
            }
        }
        return false;
    }

    @Override
    public ICommand getCommand(String name) {
        if (commandList == null || Strings.isNullOrEmpty(name)) return null;
        for (Map.Entry<String, List<ICommand>> entry : commandList.entrySet()) {
            List<ICommand> commands = entry.getValue();
            if (commands != null && commands.size() > 0) {
                for (ICommand command : commands) {
                    if (command != null && command.getName().equalsIgnoreCase(name))
                        return command;
                }
            }
        }
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
        List<String> commandNames = Lists.newArrayList();
        for (Map.Entry<String, List<String>> entry : commandNameList.entrySet()) {
            List<String> names = entry.getValue();
            if (names != null && names.size() > 0) commandNames.addAll(names);
        }
        return commandNames;
    }
}
