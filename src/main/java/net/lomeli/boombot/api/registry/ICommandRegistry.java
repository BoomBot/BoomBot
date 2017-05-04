package net.lomeli.boombot.api.registry;

import java.util.List;
import java.util.Map;

import net.lomeli.boombot.api.commands.ICommand;

public interface ICommandRegistry {
    /**
     * Register a new command
     *
     * @param addonInstance
     * @param command
     * @return true if the command was successfully registered.
     */
    boolean addCommand(Object addonInstance, ICommand command);

    /**
     * @param name
     * @return A command with the provided name, if one exists.
     */
    ICommand getCommand(String name);

    /**
     * @param addonID
     * @return A list of commands registered under the
     * addon ID
     */
    List<String> getAddonCommands(String addonID);

    /**
     * @return A list of BoomBot commands
     */
    List<String> getCommands();

    /**
     * @return A list of all registered commands
     */
    Map<String, List<String>> getFullCommandList();
}