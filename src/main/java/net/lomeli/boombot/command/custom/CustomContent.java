package net.lomeli.boombot.command.custom;

public class CustomContent {
    private String commandName, commandContent;

    public CustomContent(String commandName, String commandContent) {
        this.commandName = commandName.toLowerCase();
        this.commandContent = commandContent;
    }

    public String getCommandContent() {
        return commandContent;
    }

    public String getCommandName() {
        return commandName;
    }
}
