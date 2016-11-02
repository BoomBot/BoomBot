package net.lomeli.boombot.command.test;

import net.lomeli.boombot.api.commands.CommandInterface;
import net.lomeli.boombot.api.commands.ICommand;

public class TestCommand implements ICommand {
    @Override
    public String execute(CommandInterface cmd) {
        return "This is a test command! " + cmd.getMessage();
    }

    @Override
    public String getName() {
        return "testcommand";
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return true;
    }

    @Override
    public boolean canBotExecute(CommandInterface cmd) {
        return true;
    }

    @Override
    public String failedToExecuteMessage(CommandInterface cmd) {
        return null;
    }
}
