package net.lomeli.boombot.command.admin;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.commands.CommandInterface;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.lib.util.BotPermissionUtil;

public class ShutdownCommand implements ICommand {
    @Override
    public String execute(CommandInterface cmd) {
        BoomBot.mainListener.scheduleShutdown = true;
        return "%u is shutting down BoomBot";
    }

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return BotPermissionUtil.isUserBoomBotAdmin(cmd.getUserID());
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
