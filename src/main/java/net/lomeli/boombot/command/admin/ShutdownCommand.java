package net.lomeli.boombot.command.admin;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.permissions.BotPermission;

public class ShutdownCommand implements ICommand {
    @Override
    public CommandResult execute(CommandData cmd) {
        BoomBot.mainListener.scheduleShutdown = true;
        return new CommandResult("boombot.command.shutdown");
    }

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public boolean canUserExecute(CommandData cmd) {
        return BotPermission.isUserBoomBotAdmin(cmd.getUserInfo().getUserID());
    }

    @Override
    public boolean canBotExecute(CommandData cmd) {
        return true;
    }

    @Override
    public CommandResult failedToExecuteMessage(CommandData cmd) {
        return new CommandResult("boombot.command.shutdown.failed").setPrivateMessage(true);
    }
}
