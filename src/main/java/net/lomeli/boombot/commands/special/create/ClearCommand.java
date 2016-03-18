package net.lomeli.boombot.commands.special.create;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear-commands", "All commands for server %s has been cleared by %s.");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        cmd.sendMessage(getContent(), cmd.getGuild().getName(), cmd.getUser().getUsername());
        BoomBot.config.clearGuildCommands(cmd.getGuild());
    }

    @Override
    public boolean canExecuteCommand(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_SERVER);
    }
}
