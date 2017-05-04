package net.lomeli.boombot.api.registry;

import java.util.List;
import java.util.Map;

import net.lomeli.boombot.api.commands.ICommand;

public interface ICommandRegistry {
    boolean addCommand(Object addonInstance, ICommand command);

    ICommand getCommand(String name);

    List<String> getAddonCommands(String addonID);

    List<String> getCommands();

    Map<String, List<String>> getFullCommandList();
}