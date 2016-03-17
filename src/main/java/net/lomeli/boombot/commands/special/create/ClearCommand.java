package net.lomeli.boombot.commands.special.create;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear-command", "All commands for server %s has been cleared by %s.");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        cmd.sendMessage(getContent(), cmd.getGuild().getName(), cmd.getUser().getUsername());
        BoomBot.config.clearGuildCommands(cmd.getGuild());
    }

    @Override
    public boolean canExecuteCommand(CommandInterface cmd) {
        List<Role> userRoles = cmd.getGuild().getRolesForUser(cmd.getUser());
        for (Role role : userRoles) {
            if (role != null && role.getPermissions() != null && role.getPermissions().contains(Permission.MANAGE_SERVER))
                return true;
        }
        return false;
    }
}
