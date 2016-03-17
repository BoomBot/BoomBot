package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;

import java.util.List;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.BotPermissions;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.Logger;

public class KickCommand extends Command {
    public KickCommand() {
        super("kick", "%s has been kicked from the guild");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 1) {
            User user = getUser(cmd.getArgs().get(0), cmd.getGuild());
            if (user != null) {
                String reason = "";
                if (cmd.getArgs().size() >= 2) {
                    for (int i = 1; i < cmd.getArgs().size(); i++)
                        reason += cmd.getArgs().get(i) + " ";
                }
                if (!reason.isEmpty())
                    reason = "Reason: " + reason;
                cmd.getGuild().getManager().kick(user);
                cmd.sendMessage("%s has been kicked! %s", user.getUsername(), reason);
            } else
                cmd.sendMessage("Cannot kick %s, as they DO NOT exist.", cmd.getArgs().get(0));
        } else
            cmd.sendMessage("Kick who?");
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
        if (!BotPermissions.hasPermissions(Permission.KICK_MEMBERS, cmd.getGuild())) {
            String s = "BoomBot does not have enough permissions to kick users. Please give BoomBot a role that can kick users to use this command.";
            Logger.info(s);
            cmd.sendMessage(s);
            return false;
        }
        for (Role role : userRoles) {
            if (role != null && role.getPermissions() != null && role.getPermissions().contains(Permission.KICK_MEMBERS))
                return true;
        }
        return false;
    }
}
