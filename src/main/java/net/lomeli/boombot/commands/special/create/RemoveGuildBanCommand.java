package net.lomeli.boombot.commands.special.create;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class RemoveGuildBanCommand extends Command {
    public RemoveGuildBanCommand() {
        super("remove-ban-command", "%s can use commands in %s again.");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 1) {
            for (int i = 0; i < cmd.getArgs().size(); i++) {
                User user = getUser(cmd.getArgs().get(i), cmd.getGuild());
                if (user != null) {
                    BoomBot.config.removeCommandBan(cmd.getGuild(), user);
                    user.getPrivateChannel().sendMessage(String.format("You can now user commands again in %s.", cmd.getGuild().getName()));
                    cmd.sendMessage(getContent(), user.getUsername(), cmd.getGuild().getName());
                } else
                    cmd.sendMessage("% wasn't banned from using commands.", cmd.getArgs().get(i));
            }
        } else
            cmd.sendMessage("Ban who from using commands?");
    }

    private User getUser(String name, Guild guild) {
        for (User user : guild.getUsers()) {
            if (user != null && user.getUsername().equalsIgnoreCase(name))
                return user;
        }
        return null;
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
