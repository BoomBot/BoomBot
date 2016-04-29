package net.lomeli.boombot.commands.special.moderate;

import net.dv8tion.jda.Permission;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class UnrestrictCommand extends Command {
    public UnrestrictCommand() {
        super("unrestrict", "boombot.command.unrestrict");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (!cmd.getGuildOptions().isChannelRestricted(cmd.getChannel())) {
            cmd.sendMessage(getContent() + ".notrestricted", cmd.getChannel().getName());
            return;
        }
        cmd.getGuildOptions().freeChannel(cmd.getChannel());
        cmd.sendMessage(getContent(), cmd.getChannel().getName());
        BoomBot.configLoader.writeConfig();
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
