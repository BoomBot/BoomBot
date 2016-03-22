package net.lomeli.boombot.commands.special.create;

import net.dv8tion.jda.Permission;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class RemoveCommand extends Command {
    public RemoveCommand() {
        super("remove-command", "boombot.command.removecommand");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 1) {
            for (int i = 0; i < cmd.getArgs().size(); i++) {
                String name = cmd.getArgs().get(i);
                if (name.isEmpty())
                    cmd.sendMessage(getContent() + ".empty");
                if (BoomBot.config.removeGuildCommand(cmd.getGuild(), name))
                    cmd.sendMessage(getContent(), name, cmd.getUser().getUsername());
                else
                    cmd.sendMessage(getContent() + ".notexists", name);
            }
        } else
            cmd.sendMessage(getContent() + ".missing");
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
