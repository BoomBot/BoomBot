package net.lomeli.boombot.commands.special.moderate;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;

public class UnBanUseCommand extends Command {
    public UnBanUseCommand() {
        super("unban-use", "%s can use commands in %s again.");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 1) {
            for (int i = 0; i < cmd.getArgs().size(); i++) {
                User user = getUser(cmd.getArgs().get(i), cmd.getGuild());
                if (user != null) {
                    cmd.getGuildOptions().removeBannedUser(user);
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
    public boolean canUserExecute(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL);
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        String permissionLang = "Channel Management";
        return String.format("%s requires %s permissions to use %s", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
    }
}
