package net.lomeli.boombot.command.custom;

import com.google.common.base.Strings;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.permissions.BotPermission;

public class MakeCommand implements ICommand {
    @Override
    public CommandResult execute(CommandData cmd) {
        if (Strings.isNullOrEmpty(cmd.getMessage()) || cmd.getArgs().size() < 2)
            return new CommandResult("boombot.command.make.empty");
        String name = cmd.getArgs().get(0);
        if (Strings.isNullOrEmpty(name)) return new CommandResult("boombot.command.make.name.missing");
        if (CustomRegistry.INSTANCE.getGuildCommand(cmd.getGuildID(), name) != null)
            return new CommandResult("boombot.command.make.name.used");
        if (BoomBot.commandRegistry.getCommand(name) != null)
            return new CommandResult("boombot.command.make.name.used");
        int subStart = name.length() + 1;
        if (subStart >= cmd.getMessage().length()) return new CommandResult("boombot.command.make.empty");
        String messageContent = cmd.getMessage().substring(subStart);
        if (Strings.isNullOrEmpty(messageContent)) return new CommandResult("boombot.command.make.empty");
        String safeMessage = messageContent.replaceAll("%s", "(Blank)").replaceAll("%S", "(BLANK)");
        if (CustomRegistry.INSTANCE.addGuildCommand(cmd.getGuildID(), new CustomContent(name, messageContent)))
            return new CommandResult("boombot.command.make", name, safeMessage);
        return new CommandResult("boombot.command.make.error");
    }

    @Override
    public String getName() {
        return "makecom";
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
        return null;
    }
}
