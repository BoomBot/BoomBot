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
import net.lomeli.boombot.lib.GuildOptions;
import net.lomeli.boombot.lib.Logger;

public class KickCommand extends Command {
    public KickCommand() {
        super("kick", "boombot.command.kick");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 1) {
            User user = getUser(cmd.getArgs().get(0), cmd.getGuild());
            if (user != null) {
                if (user.getId().equals(BoomBot.jda.getSelfInfo().getId())) {
                    cmd.sendMessage(getContent() + ".self");
                    return;
                }
                String reason = "";
                if (cmd.getArgs().size() >= 2) {
                    for (int i = 1; i < cmd.getArgs().size(); i++)
                        reason += cmd.getArgs().get(i) + " ";
                }
                if (!reason.isEmpty())
                    reason = cmd.getGuildOptions().translate(getContent() + ".reason", reason);
                cmd.getGuild().getManager().kick(user);
                user.getPrivateChannel().sendMessage(cmd.getGuildOptions().translate(getContent() + ".message", cmd.getGuild().getName(), reason));
                cmd.sendMessage(getContent(), user.getUsername(), reason);
            } else
                cmd.sendMessage(getContent() + ".cannot", cmd.getArgs().get(0));
        } else
            cmd.sendMessage(getContent() + ".who");
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
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.KICK_MEMBERS);
    }

    @Override
    public boolean canBoomBotExecute(CommandInterface cmd) {
        return PermissionsHelper.hasPermissions(Permission.KICK_MEMBERS, cmd.getGuild());
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        String permissionLang = options.translate("permissions.abilities.kick");
        String message = options.translate("boombot.command.permissions.user.missing", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
        if (userType.isBoomBot()) {
            String s = "%1$s does not have enough permissions to kick users. Please give %1$s a role that can kick users to use this command.";
            Logger.info(s, BoomBot.jda.getSelfInfo().getUsername());
            message = options.translate("boombot.command.permissions.user.missing", BoomBot.jda.getSelfInfo().getUsername(), permissionLang, cmd.getCommand());
        }
        return message;
    }
}
