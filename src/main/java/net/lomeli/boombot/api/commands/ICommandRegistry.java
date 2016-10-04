package net.lomeli.boombot.api.commands;

import java.util.List;

public interface ICommandRegistry {
    boolean addCommand(Object addonInstance, Command command);
    Command getCommand(String name);
    List<String> getAddonCommands(String addonID);
    List<String> getCommands();
}
