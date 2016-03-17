package net.lomeli.boombot.commands.special;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class GuildIdCommand extends Command {
    public GuildIdCommand() {
        super("guild-id", "%s Guild ID is %s");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        cmd.sendMessage(getContent(), cmd.getGuild().getName(), cmd.getGuild().getId());
    }
}
