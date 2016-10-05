package net.lomeli.boombot.command.custom;

import com.google.common.base.Strings;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.commands.Command;
import net.lomeli.boombot.api.commands.CommandInterface;
import net.lomeli.boombot.lib.DataRegistry;

public class MakeCommand implements Command {
    @Override
    public String execute(CommandInterface cmd) {
        if (Strings.isNullOrEmpty(cmd.getMessage()) || cmd.getArgs().size() < 2) return "Cannot make empty command";
        String name = cmd.getArgs().get(0);
        if (Strings.isNullOrEmpty(name)) return "Command requires name";
        if (CustomRegistry.INSTANCE.getGuildCommand(cmd.getGuildID(), name) != null)
            return "Custom command name already in use";
        if (BoomAPI.commandRegistry.getCommand(name) != null) return "Command name already in use";
        int subStart = name.length() + 1;
        if (subStart >= cmd.getMessage().length()) return "Cannot make empty command";
        String messageContent = cmd.getMessage().substring(subStart);
        if (Strings.isNullOrEmpty(messageContent)) return "Cannot make empty command";
        if (CustomRegistry.INSTANCE.addGuildCommand(cmd.getGuildID(), new CustomContent(name, messageContent)))
            ((DataRegistry) BoomAPI.dataRegistry).writeGuildData(cmd.getGuildID());
        return "Created command " + name + ". Content: " + messageContent;
    }

    @Override
    public String getName() {
        return "makecom";
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return false;
    }

    @Override
    public boolean canBotExecute(CommandInterface cmd) {
        return false;
    }

    @Override
    public String failedToExecuteMessage(CommandInterface cmd) {
        return null;
    }
}
