package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;
import net.lomeli.boombot.helper.Logger;

public class StopBotCommand extends Command {

    public StopBotCommand() {
        super("stop-boom-bot", "boombot.command.stopboombot");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (!BoomBot.debug) {
            List<Guild> guilds = BoomBot.jda.getGuilds();
            if (!guilds.isEmpty())
                guilds.stream().filter(guild -> guild != null).forEach(guild -> {
                    GuildOptions options = BoomBot.config.getGuildOptions(guild);
                    if (options != null && options.announceStopped())
                        guild.getPublicChannel().sendMessage(options.translate(getContent()));
                });
        }
        Logger.info("BoomBot shutting down via command from %s...", cmd.getUser().getUsername());
        BoomBot.jda.shutdown();
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_SERVER);
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        String permissionLang = options.translate("permissions.manage.server");
        return options.translate("boombot.command.permissions.user.missing", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
    }
}
