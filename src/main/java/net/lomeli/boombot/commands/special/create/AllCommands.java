package net.lomeli.boombot.commands.special.create;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.commands.CommandRegistry;
import net.lomeli.boombot.lib.CommandInterface;

public class AllCommands extends Command {
    public AllCommands() {
        super("all-commands", "Commands for %s: %s");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        String commandList = "";
        for (Command c : CommandRegistry.INSTANCE.getCommands()) {
            if (c != null)
                commandList += "!" + c.getName() + ", ";
        }
        cmd.sendMessage("Default Commands: %s", commandList.substring(0, commandList.length() - 2));
        commandList = "";
        for (Command c : BoomBot.config.getCommandsForGuild(cmd.getGuild())) {
            if (c != null)
                commandList += "!" + c.getName() + ", ";
        }
        cmd.sendMessage(getContent(), cmd.getGuild().getName(), commandList.substring(0, commandList.length() - 2));
    }
}
