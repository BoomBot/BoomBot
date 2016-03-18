package net.lomeli.boombot.commands.special.create;

import net.dv8tion.jda.Permission;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;

public class RemoveCommand extends Command {
    public RemoveCommand() {
        super("remove-command", "%s has been removed by %s.");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 1) {
            for (int i = 0; i < cmd.getArgs().size(); i++) {
                String name = cmd.getArgs().get(i);
                if (name.isEmpty())
                    cmd.sendMessage("Command name cannot be empty!");
                if (BoomBot.config.removeGuildCommand(cmd.getGuild(), name))
                    cmd.sendMessage(getContent(), name, cmd.getUser().getUsername());
                else
                    cmd.sendMessage("Command %s does NOT exist!", name);
            }
        } else
            cmd.sendMessage("Missing Argument: Command(s) name");
    }

    @Override
    public boolean canExecuteCommand(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL);
    }
}
