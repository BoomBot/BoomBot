package net.lomeli.boombot.commands.special.create;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;

public class BanUseCommand extends Command {
    public BanUseCommand() {
        super("ban-use", "%s has been banned from using commands in %s! %s");
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
                BoomBot.config.banUserCommands(cmd.getGuild(), user);
                user.getPrivateChannel().sendMessage(String.format("You have been banned from using commands in %s. %s", cmd.getGuild().getName(), reason));
                cmd.sendMessage(getContent(), user.getUsername(), cmd.getGuild().getName(), reason);
            } else
                cmd.sendMessage("Cannot ban %s from using commands, as they DO NOT exist.", cmd.getArgs().get(0));
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
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL);
    }
}
