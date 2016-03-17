package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.Logger;
import net.lomeli.boombot.lib.CommandInterface;

public class StopBotCommand extends Command {

    public StopBotCommand() {
        super("stop-boom-bot", "BoomBot is now shutting down...");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        List<Guild> guilds = BoomBot.jda.getGuilds();
        for (Guild g : guilds) {
            g.getPublicChannel().sendMessage(getContent());
        }
        Logger.info("BoomBot shutting down via command from %s...", cmd.getUser().getUsername());
        BoomBot.run = false;
        System.exit(0);
    }

    @Override
    public boolean canExecuteCommand(CommandInterface cmd) {
        List<Role> userRoles = cmd.getGuild().getRolesForUser(cmd.getUser());
        for (Role role : userRoles) {
            if (role != null && role.getPermissions() != null && role.getPermissions().contains(Permission.MANAGE_SERVER))
                return true;
        }
        return false;
    }
}
