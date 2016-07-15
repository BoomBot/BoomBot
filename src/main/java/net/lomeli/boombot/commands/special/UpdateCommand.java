package net.lomeli.boombot.commands.special;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class UpdateCommand extends Command {
    public UpdateCommand() {
        super("update", "boombot.command.update");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (!BoomBot.config.isUpdatable()) {
            cmd.sendMessage(getContent() + ".disabled");
            return;
        }
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return BoomBot.config.isUserAdmin(cmd.getUser());
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        String permissionLang = options.translate("permissions.manage.server");
        return options.translate("boombot.command.permissions.user.missing", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
    }
}
