package net.lomeli.boombot.commands.special.create;

import com.google.common.base.Strings;
import net.dv8tion.jda.Permission;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
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
            if (Strings.isNullOrEmpty(name))
                cmd.sendMessage("Command name cannot be empty!");
            if (Strings.isNullOrEmpty(content))
                cmd.sendMessage("Command cannot be empty!");
            String safeName = name.replaceAll("%s", "% s").replaceAll("%S", "% S").replaceAll("%u", "<User>").replaceAll("%U", "<USER>");
            String safeContent = content.replaceAll("%s", "(Blank)").replaceAll("%S", "(Blank)").replaceAll("%u", "<User>").replaceAll("%U", "<USER>");
            if (BoomBot.config.addGuildCommand(cmd.getGuild(), new Command(name, content)))
                cmd.sendMessage(String.format(getContent(), safeName, safeContent));
            else
                cmd.sendMessage(String.format("Command with the name %s already exists!", safeName));
        } else
            cmd.sendMessage("Cannot create command! Missing arguments");
    }

    @Override
    public boolean canExecuteCommand(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL);
    }
}
