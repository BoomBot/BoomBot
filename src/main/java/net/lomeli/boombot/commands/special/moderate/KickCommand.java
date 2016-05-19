package net.lomeli.boombot.commands.special.moderate;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.User;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.Logger;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.helper.UserHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class KickCommand extends Command {
    public KickCommand() {
        super("kick", "boombot.command.kick");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        List<User> mentionedUsers = cmd.getMessage().getMentionedUsers();
        if (mentionedUsers == null && mentionedUsers.isEmpty())
            cmd.sendMessage(getContent() + ".who");
        else {
            mentionedUsers.stream().forEach(user -> {
                if (user == null) {
                    cmd.sendMessage(getContent() + ".cannot");
                    return;
                }
                if (UserHelper.isUserBoomBot(user)) {
                    cmd.sendMessage(getContent() + ".self");
                    return;
                }
                if (UserHelper.isOwner(user, cmd.getGuild())) {
                    cmd.sendMessage(getContent() + ".owner");
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
                user.getPrivateChannel().sendMessage(options.translate(getContent() + ".message", cmd.getGuild().getName(), reason));
                cmd.sendMessage(getContent(), user.getUsername(), reason);
            });
        }
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
