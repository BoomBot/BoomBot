package net.lomeli.boombot.commands.special.create;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Role;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class CreateCommand extends Command {
    public CreateCommand() {
        super("create-command", "Creating command %s. Content: %s");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 2) {
            String name = cmd.getArgs().get(0);
            String content = "";
            for (int i = 1; i < cmd.getArgs().size(); i++) {
                String st = cmd.getArgs().get(i);
                content += st + " ";
            }
            if (name.isEmpty())
                cmd.sendMessage("Command name cannot be empty!");
            if (content.isEmpty())
                cmd.sendMessage("Command cannot be empty!");
            if (BoomBot.config.addGuildCommand(cmd.getGuild(), new Command(name, content)))
                cmd.sendMessage(String.format(getContent(), name, content.replaceAll("%s", "___")));
            else
                cmd.sendMessage(String.format("Command with name %s already exists!", name));
        } else
            cmd.sendMessage("Cannot create command! Missing arguments");
    }

    @Override
    public boolean canExecuteCommand(CommandInterface cmd) {
        List<Role> userRoles = cmd.getGuild().getRolesForUser(cmd.getUser());
        for (Role role : userRoles) {
            if (role != null && role.getPermissions() != null && role.getPermissions().contains(Permission.MANAGE_CHANNEL))
                return true;
        }
        return false;
    }
}
