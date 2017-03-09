package net.lomeli.boombot.command.custom;

import com.google.common.base.Strings;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.permissions.BotPermission;

public class RemoveCommand implements ICommand {
    @Override
    public CommandResult execute(CommandData cmd) {
        if (cmd.getArgs().size() < 1) return new CommandResult("boombot.command.rm.error.empty");
        String name = cmd.getArgs().get(0);
        if (Strings.isNullOrEmpty(name)) return new CommandResult("boombot.command.rm.error.empty");
        if (BoomBot.commandRegistry.getCommand(name) != null)
            return new CommandResult("boombot.command.rm.error.custom", name);
        CustomContent command = CustomRegistry.INSTANCE.getGuildCommand(cmd.getGuildID(), name);
        if (command == null) return new CommandResult("boombot.command.rm.error.exist", name);
        CustomRegistry.INSTANCE.removeGuildCommand(cmd.getGuildID(), name);
        String username = cmd.getUserInfo().hasNickName() ? cmd.getUserInfo().getNickName() : cmd.getUserInfo().getUserName();
        return new CommandResult("boombot.command.rm", username, name);
    }

    @Override
    public String getName() {
        return "rmcom";
    }

    @Override
    public boolean canUserExecute(CommandData cmd) {
        return BotPermission.userHavePermission(cmd.getUserInfo().getUserID(), cmd.getGuildID(), BotPermission.CUSTOM_COMMANDS);
    }

    @Override
    public boolean canBotExecute(CommandData cmd) {
        return true;
    }

    @Override
    public CommandResult failedToExecuteMessage(CommandData cmd) {
        return new CommandResult("boombot.command.rm.error.perm");
    }
}
