package net.lomeli.boombot.command.test;

import net.lomeli.boombot.api.commands.Command;
import net.lomeli.boombot.api.commands.CommandInterface;

public class TestCommand implements Command {
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
