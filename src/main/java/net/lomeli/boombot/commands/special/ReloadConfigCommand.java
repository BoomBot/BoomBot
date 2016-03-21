package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;

public class ReloadConfigCommand extends Command {

    public ReloadConfigCommand() {
        super("reload-config", "Configs successfully reloaded!");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        super.executeCommand(cmd);
        BoomBot.configLoader.parseConfig();
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_SERVER);
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        String permissionLang = "Server Management";
        return String.format("%s requires %s permissions to use %s", cmd.getUser().getUsername(), permissionLang, cmd.getCommand());
    }
}
