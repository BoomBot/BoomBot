package net.lomeli.boombot.command.admin;

import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.permissions.BotPermission;
import net.lomeli.boombot.core.AutoSaveThread;

public class SaveDelayCommand implements ICommand {
    @Override
    public CommandResult execute(CommandData cmd) {
        if (cmd.getArgs().size() > 1)
            return new CommandResult("boombot.command.savedelay.error.onearg").setPrivateMessage(true);
        long i = Long.parseLong(cmd.getArgs().get(0));
        if (cmd.getArgs().isEmpty() || i < 1)
            return new CommandResult("boombot.command.savedelay.error.specify").setPrivateMessage(true);
        if (i < 300000) return new CommandResult("boombot.command.savedelay.error.few");
        AutoSaveThread.SAVE_DELAY = i;
        long min = 60000;
        return new CommandResult("boombot.command.savedelay", i, min).setPrivateMessage(true);
    }

    @Override
    public String getName() {
        return "autosavedelay";
    }

    @Override
    public boolean canUserExecute(CommandData cmd) {
        return BotPermission.isUserBoomBotAdmin(cmd.getUserInfo().getUserID());
    }

    @Override
    public boolean canBotExecute(CommandData cmd) {
        return canUserExecute(cmd);
    }

    @Override
    public CommandResult failedToExecuteMessage(CommandData cmd) {
        return new CommandResult("boombot.command.shutdown.failed").setPrivateMessage(true);
    }
}
