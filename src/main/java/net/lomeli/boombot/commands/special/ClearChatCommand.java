package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.ChannelHelper;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;

public class ClearChatCommand extends Command {

    public ClearChatCommand() {
        super("clear-chat", "");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        ChannelHelper.getChannelMessages(cmd.getChannel(), cmd.getMessage());
    }

    @Override
    public boolean canExecuteCommand(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_SERVER) &&
                PermissionsHelper.hasPermissions(Permission.MANAGE_SERVER, cmd.getGuild());
    }
}
