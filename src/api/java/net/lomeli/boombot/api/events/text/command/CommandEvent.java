package net.lomeli.boombot.api.events.text.command;

import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.events.text.MessageEvent;

/**
 * Fires whenever a user uses a command.
 */
@Event.Cancelable
public class CommandEvent extends MessageEvent {
    private final ICommand command;

    public CommandEvent(ICommand command, CommandData cmd) {
        super(cmd.getUserInfo(), cmd.getChannelID(), cmd.getGuildID(), cmd.getMessage(), cmd.getMessage());
        this.command = command;
    }

    public ICommand getCommand() {
        return command;
    }
}
