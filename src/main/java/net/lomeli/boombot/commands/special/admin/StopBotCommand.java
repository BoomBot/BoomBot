package net.lomeli.boombot.commands.special.admin;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.logging.BoomLogger;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public class StopBotCommand extends Command {

    public StopBotCommand() {
        super("stop-boom-bot", "boombot.command.stopboombot");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        super.executeCommand(cmd);
        BoomLogger.info("BoomBot shutting down via command from %s...", cmd.getUser().getUsername());
        BoomBot.shutdownBoomBot();
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
