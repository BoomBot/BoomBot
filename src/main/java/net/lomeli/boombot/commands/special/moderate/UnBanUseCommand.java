package net.lomeli.boombot.commands.special.moderate;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.User;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.helper.UserHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class UnBanUseCommand extends Command {
    public UnBanUseCommand() {
        super("unbancom", "boombot.command.unbanuse");
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
                    cmd.sendMessage(getContent() + ".who");
                    return;
                }
                if (UserHelper.isUserBoomBot(user)) {
                    cmd.sendMessage(getContent() + ".self");
                    return;
                }
                if (options.isUserBanned(user)) {
                    cmd.sendMessage(getContent() + ".wasnt", user.getUsername());
                    return;
                }
                BoomBot.config.removeCommandBan(cmd.getGuildOptions(), user);
                user.getPrivateChannel().sendMessage(options.translate(getContent() + ".message", cmd.getGuild().getName()));
                cmd.sendMessage(getContent(), user.getUsername(), cmd.getGuild().getName());
            });
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
