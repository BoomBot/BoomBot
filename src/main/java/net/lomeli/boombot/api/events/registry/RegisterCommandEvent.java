package net.lomeli.boombot.api.events.registry;

import net.lomeli.boombot.api.registry.ICommandRegistry;
import net.lomeli.boombot.api.events.Event;

/**
 * Use this event to register addon commands.
 */
public class RegisterCommandEvent extends Event {

    private final ICommandRegistry commandRegistry;

    public RegisterCommandEvent(ICommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    public ICommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}
