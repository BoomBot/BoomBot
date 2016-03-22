package net.lomeli.boombot.commands.special.moderate;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class BanUseCommand extends Command {
    public BanUseCommand() {
        super("ban-use", "boombot.command.banuse");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        if (cmd.getArgs().size() >= 1) {
            User user = getUser(cmd.getArgs().get(0), cmd.getGuild());
            if (user != null) {
                String reason = "";
                if (cmd.getArgs().size() >= 2) {
                    for (int i = 1; i < cmd.getArgs().size(); i++)
                        reason += cmd.getArgs().get(i) + " ";
                }
                if (!reason.isEmpty())
                    reason = options.translate(getContent() + ".reason", reason);
                BoomBot.config.banUserCommands(cmd.getGuild(), user);
                user.getPrivateChannel().sendMessage(options.translate(getContent() + ".message", cmd.getGuild().getName(), reason));
                cmd.sendMessage(getContent(), user.getUsername(), cmd.getGuild().getName(), reason);
            } else
                cmd.sendMessage(getContent() + ".cannotban", cmd.getArgs().get(0));
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
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL);
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        String permissionLang = options.translate("permissions.manage.channel");
        return options.translate("boombot.command.permissions.user.missing", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
    }
}
