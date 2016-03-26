package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.commands.CommandRegistry;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "boombot.command.help");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        if (options.isChannelRestricted(cmd.getChannel()) && !PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL)) {
            cmd.sendUserMessage(getContent() + ".restricted", cmd.getChannel().getName());
            return;
        }
        String msg = "";
        String commandList = "";
        for (Command c : CommandRegistry.INSTANCE.getCommands()) {
            if (c != null)
                commandList += options.getCommandKey() + c.getName() + ", ";
        }
        msg += options.translate(getContent() + ".default", commandList.substring(0, commandList.length() - 2));
        commandList = "";
        for (Command c : options.getCommandList()) {
            if (c != null)
                commandList += options.getCommandKey() + c.getName() + ", ";
        }
        msg += "\n" + options.translate(getContent(), cmd.getGuild().getName(), commandList.substring(0, commandList.length() - 2));
        cmd.sendMessage(msg);
    }
}
