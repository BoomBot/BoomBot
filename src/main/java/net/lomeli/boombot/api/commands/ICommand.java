package net.lomeli.boombot.api.commands;

public interface ICommand {
    /**
     * Processes the command arguments and executes the command.
     * @param data - Basic information about the user,
     *             the guild, channel, arguments, and the full message.
     * @return null or a CommandResult. CommandResult will send a message
     * back to the channel or to the user themselves if set to private
     */
    CommandResult execute(CommandData data);

    /**
     * @return name of the command. Must be unique, as it'll
     * be what users use to execute the command
     */
    String getName();

    /**
     * @param data - Basic information about the user,
     *             the guild, channel, arguments, and the full message.
     * @return true if the user can use the command
     */
    boolean canUserExecute(CommandData data);

    /**
     * @param data - Basic information about the user,
     *             the guild, channel, arguments, and the full message.
     * @return true if BoomBot can use the command
     */
    boolean canBotExecute(CommandData data);

    /**
     * @param data - Basic information about the user,
     *             the guild, channel, arguments, and the full message.
     * @return a CommandResult if either the user or BoomBot
     * fail either {@link #canUserExecute(CommandData)} or
     * {@link #canBotExecute(CommandData)} respectively.
     */
    CommandResult failedToExecuteMessage(CommandData data);
}
