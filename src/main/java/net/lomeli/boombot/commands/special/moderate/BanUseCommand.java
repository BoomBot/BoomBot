package net.lomeli.boombot.commands.special.moderate;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.helper.UserHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class BanUseCommand extends Command {
    public BanUseCommand() {
        super("bancom", "boombot.command.banuse");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        List<User> mentionedUsers = cmd.getMessage().getMentionedUsers();
        if (mentionedUsers == null && mentionedUsers.isEmpty())
            cmd.sendMessage(getContent() + ".who");
        else if (mentionedUsers.size() > 1)
            cmd.sendMessage(getContent() + ".multiple");
        else {
            User user = mentionedUsers.get(0);
            if (user != null) {
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
                    reason = options.translate(getContent() + ".reason", reason);
                BoomBot.config.banUserCommands(cmd.getGuildOptions(), user);
                user.getPrivateChannel().sendMessage(options.translate(getContent() + ".message", cmd.getGuild().getName(), reason));
                cmd.sendMessage(getContent(), user.getUsername(), cmd.getGuild().getName(), reason);
            } else
                cmd.sendMessage(getContent() + ".cannotban", cmd.getArgs().get(0));
        }
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL);
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        String permissionLang = options.translate("permissions.manage.channel");
        return options.translate("boombot.command.permissions.user.missing", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
    }
}
