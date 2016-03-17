package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class ReloadConfigCommand extends Command {

    public ReloadConfigCommand() {
        super("reload-config", "Configs successfully reloaded!");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        super.executeCommand(cmd);
        BoomBot.configLoader.parseConfig();
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
