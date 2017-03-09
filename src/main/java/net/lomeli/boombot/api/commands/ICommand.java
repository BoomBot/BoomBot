package net.lomeli.boombot.api.commands;

public interface ICommand {
    CommandResult execute(CommandData cmd);

    String getName();

    boolean canUserExecute(CommandData cmd);

    boolean canBotExecute(CommandData cmd);

    CommandResult failedToExecuteMessage(CommandData cmd);
}
