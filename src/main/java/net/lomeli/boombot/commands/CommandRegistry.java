package net.lomeli.boombot.commands;

import com.google.common.collect.Lists;

import java.util.List;

import net.lomeli.boombot.commands.special.CreateCommand;
import net.lomeli.boombot.commands.special.KickCommand;
import net.lomeli.boombot.commands.special.RunningCommand;
import net.lomeli.boombot.commands.special.StopBotCommand;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.Logger;

public enum CommandRegistry {
    INSTANCE();

    private List<Command> commands;

    CommandRegistry() {
        commands = Lists.newArrayList();
        registerBasicCommands();
    }

    private void registerBasicCommands() {
        addNewCommand(new StopBotCommand());
        addNewCommand(new CreateCommand());
        addNewCommand(new KickCommand());
        addNewCommand(new RunningCommand());
        addNewCommand(new Command("wub", "Wubalubadubdub!"));
    }

    public boolean addNewCommand(Command command) {
        for (Command c : commands) {
            if (c.getName().equalsIgnoreCase(command.getName())) {
                Logger.info("Command with name \"%s\" already exists, ignoring...", command.getName());
                return false;
            }
        }
        commands.add(command);
        return true;
    }

    public void executeCommand(CommandInterface cmd) {
        for (Command c : commands) {
            if (c.getName().equalsIgnoreCase(cmd.getCommand())) {
                if (c.canExecuteCommand(cmd)) {
                    Logger.info("%s used %s command.", cmd.getUser().getUsername(), cmd.getCommand());
                    c.executeCommand(cmd);
                } else
                    cmd.sendMessage("%s does not have enough permissions to use %s command!", cmd.getUser().getUsername(), cmd.getCommand());
                return;
            }
        }
    }
}
