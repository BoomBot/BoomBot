package net.lomeli.boombot.commands;

import net.lomeli.boombot.lib.CommandInterface;

public class Command {
    private String name;
    private String content;

    public Command(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public void executeCommand(CommandInterface cmd) {
        cmd.sendMessage(String.format(getContent(), cmd.getArgs()));
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public boolean canExecuteCommand(CommandInterface cmd) {
        return true;
    }
}
