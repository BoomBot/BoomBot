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
import net.lomeli.boombot.lib.Logger;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban", "boombot.command.ban");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        List<User> mentionedUsers = cmd.getMessage().getMentionedUsers();
        if (mentionedUsers == null && mentionedUsers.isEmpty())
            cmd.sendMessage(getContent() + ".who");
        else if (mentionedUsers.size() > 1)
            cmd.sendMessage(getContent() + ".multiple");
        else if (cmd.getArgs().size() < 2)
            cmd.sendMessage(getContent() + ".days.missing");
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
                int daysDelete = parseInt(cmd.getArgs().get(1));
                if (daysDelete <= -1) {
                    cmd.sendMessage(getContent() + ".days.zero");
                    return;
                }
                String reason = "";
                if (cmd.getArgs().size() >= 3) {
                    for (int i = 2; i < cmd.getArgs().size(); i++)
                        reason += cmd.getArgs().get(i) + " ";
                }
                if (!reason.isEmpty())
                    reason = options.translate(getContent() + ".reason", reason);
                cmd.getGuild().getManager().ban(user, daysDelete);
                user.getPrivateChannel().sendMessage(options.translate(getContent() + ".message", cmd.getGuild().getName(), reason));
                cmd.sendMessage(getContent(), user.getUsername(), cmd.getGuild().getName(), reason);
            } else
                cmd.sendMessage(getContent() + ".cannot", cmd.getArgs().get(0));
        }
    }

    private int parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            Logger.error("Number format exception in Ban Command", e);
        }
        return 0;
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.BAN_MEMBERS);
    }

    @Override
    public boolean canBoomBotExecute(CommandInterface cmd) {
        return PermissionsHelper.hasPermissions(Permission.BAN_MEMBERS, cmd.getGuild());
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        String permissionLang = options.translate("permissions.abilities.ban");
        String message = options.translate("boombot.command.permissions.user.missing", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
        if (userType.isBoomBot()) {
            String s = "%1$s does not have enough permissions to ban users. Please give %1$s a role that can ban users to use this command.";
            Logger.info(s, BoomBot.jda.getSelfInfo().getUsername());
            message = options.translate("boombot.command.permissions.user.missing", BoomBot.jda.getSelfInfo().getUsername(), permissionLang, cmd.getCommand());
        }
        return message;
    }
}