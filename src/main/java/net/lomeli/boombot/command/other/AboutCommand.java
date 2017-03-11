package net.lomeli.boombot.command.other;

import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.util.GuildUtil;

public class AboutCommand implements ICommand {

    @Override
    public CommandResult execute(CommandData cmd) {
        return new CommandResult("boombot.command.about",
                GuildUtil.getGuildCommandKey(cmd.getGuildID())).setPrivateMessage(true);
    }

    @Override
    public String getName() {
        return "about";
    }

    @Override
    public boolean canUserExecute(CommandData cmd) {
        return true;
    }

    @Override
    public boolean canBotExecute(CommandData cmd) {
        return true;
    }

    @Override
    public CommandResult failedToExecuteMessage(CommandData cmd) {
        return null;
    }
}
