package net.lomeli.boombot.commands.special.create;

import net.dv8tion.jda.Permission;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.commands.CommandRegistry;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Commands for %s: %s");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        GuildOptions options = BoomBot.config.getGuildOptions(cmd.getGuild());
        if (options.isChannelRestricted(cmd.getChannel()) && !PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL)) {
            cmd.sendUserMessage("Custom commands are restricted in the %s channel", cmd.getChannel().getName());
            return;
        }
        String commandList = "";
        for (Command c : CommandRegistry.INSTANCE.getCommands()) {
            if (c != null)
                commandList += "!" + c.getName() + ", ";
        }
        cmd.sendMessage("Default Commands: %s", commandList.substring(0, commandList.length() - 2));
        commandList = "";
        for (Command c : options.getCommandList()) {
            if (c != null)
                commandList += "!" + c.getName() + ", ";
        }
        cmd.sendMessage(getContent(), cmd.getGuild().getName(), commandList.substring(0, commandList.length() - 2));
    }
}
