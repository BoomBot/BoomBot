package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;
import net.lomeli.boombot.lib.Logger;

public class StopBotCommand extends Command {

    public StopBotCommand() {
        super("stop-boom-bot", "BoomBot is now shutting down...");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        List<Guild> guilds = BoomBot.jda.getGuilds();
        for (Guild guild : guilds) {
            if (guild != null) {
                GuildOptions options = BoomBot.config.getGuildOptions(guild);
                if (options != null && options.announceStopped())
                    guild.getPublicChannel().sendMessage(getContent());
            }
        }
        Logger.info("BoomBot shutting down via command from %s...", cmd.getUser().getUsername());
        BoomBot.run = false;
        System.exit(0);
    }

    @Override
    public boolean canExecuteCommand(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_SERVER);
    }
}