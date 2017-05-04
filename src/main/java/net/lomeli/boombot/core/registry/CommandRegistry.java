package net.lomeli.boombot.core.registry;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.registry.ICommandRegistry;
import net.lomeli.boombot.command.admin.SaveDelayCommand;
import net.lomeli.boombot.command.admin.SetAdminCommand;
import net.lomeli.boombot.command.admin.ShutdownCommand;
import net.lomeli.boombot.command.custom.MakeCommand;
import net.lomeli.boombot.command.custom.RemoveCommand;
import net.lomeli.boombot.command.moderate.BanCommand;
import net.lomeli.boombot.command.moderate.ClearChatCommand;
import net.lomeli.boombot.command.other.AboutCommand;
import net.lomeli.boombot.command.other.ListCommand;

public class CommandRegistry implements ICommandRegistry {
    public static final String BOOMBOT_ID = "boombot";
    private Map<String, List<ICommand>> commandList;
    private Map<String, List<String>> commandNameList;

    public CommandRegistry() {
        commandList = Maps.newHashMap();
        commandNameList = Maps.newHashMap();
        initBuiltInCommands();
    }

    private void initBuiltInCommands() {
        //GENERIC COMMANDS
        addBaseCommand(new AboutCommand());
        addBaseCommand(new ListCommand());
        //ADMIN COMMANDS
        addBaseCommand(new ShutdownCommand());
        addBaseCommand(new SetAdminCommand());
        addBaseCommand(new SaveDelayCommand());
        //MOD COMMANDS
        addBaseCommand(new ClearChatCommand());
        addBaseCommand(new BanCommand());
        //CUSTOM COMMANDS
        addBaseCommand(new MakeCommand());
        addBaseCommand(new RemoveCommand());
    }

    private boolean addBaseCommand(ICommand command) {
        return addCommand(command, BOOMBOT_ID);
    }

    private boolean addCommand(ICommand command, String id) {
        if (command == null) return false;
        List<ICommand> commands = Lists.newArrayList();
        List<String> names = Lists.newArrayList();
        if (commandList.containsKey(id)) commands = commandList.get(id);
        if (commandNameList.containsKey(id)) names = commandNameList.get(id);
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
        List<ICommand> commandList = Lists.newArrayList();
        this.commandList.values().stream().filter(list ->
                list != null && !list.isEmpty()).forEach(list -> commandList.addAll(list));
        Optional<ICommand> result = Optional.ofNullable(commandList.stream().filter(command ->
                command != null && command.getName().equalsIgnoreCase(name)
        ).findFirst().orElse(null));
        return (result != null && result.isPresent()) ? result.get() : null;
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
        commandNameList.values().stream().filter(list ->
                list != null && !list.isEmpty()).forEach(list -> commandNames.addAll(list));
        return Collections.unmodifiableList(commandNames);
    }

    @Override
    public Map<String, List<String>> getFullCommandList() {
        return Collections.unmodifiableMap(this.commandNameList);
    }
}
