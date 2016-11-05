package net.lomeli.boombot.api.events.text.command;

import net.lomeli.boombot.api.commands.CommandInterface;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.events.text.MessageEvent;

@Event.Cancelable
public class CommandEvent extends MessageEvent {
    private final ICommand command;

    public CommandEvent(ICommand command, CommandInterface cmd) {
        super(cmd.getUserID(), cmd.getChannelID(), cmd.getGuildID(), cmd.getMessage(), cmd.getMessage());
        this.command = command;
    }

    public ICommand getCommand() {
        return command;
    }
}
