package net.lomeli.boombot.command.custom;

import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;

public class CustomCommand implements ICommand {
    private String key, content;

    public CustomCommand(String key, String content) {
        this.key = key;
        this.content = content;
    }

    public CustomCommand(CustomContent content) {
        this(content.getCommandName(), content.getCommandContent());
    }

    public String getContent() {
        return content;
    }

    @Override
    public CommandResult execute(CommandData cmd) {
        return null;
    }

    @Override
    public String getName() {
        return key;
    }

    @Override
    public boolean canUserExecute(CommandData cmd) {
        return false;
    }

    @Override
    public boolean canBotExecute(CommandData cmd) {
        return false;
    }

    @Override
    public CommandResult failedToExecuteMessage(CommandData cmd) {
        return null;
    }
}
