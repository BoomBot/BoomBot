package net.lomeli.boombot.commands.special.debug;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class ChannelIdCommand extends Command {
    public ChannelIdCommand() {
        super("channel-id", "%s Channel ID is %s");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        cmd.sendMessage(getContent(), cmd.getChannel().getName(), cmd.getChannel().getId());
    }
}
