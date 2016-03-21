package net.lomeli.boombot.commands.special.moderate;

import net.dv8tion.jda.Permission;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;

public class DerestrictCommand extends Command {
    public DerestrictCommand() {
        super("derestrict", "Command restrictions have been lifted from %s.");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (!cmd.getGuildOptions().isChannelRestricted(cmd.getChannel())) {
            cmd.sendMessage("%s isn't in restricted use mode.", cmd.getChannel().getName());
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
        String permissionLang = "Channel Management";
        return String.format("%s requires %s permissions to use %s", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
    }
}
