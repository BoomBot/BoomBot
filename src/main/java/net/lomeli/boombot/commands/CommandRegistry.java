package net.lomeli.boombot.commands;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.special.*;
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
        addNewCommand(new RemoveCommand());
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

    public boolean addCustomCommand(Command command) {
        if (addNewCommand(command)) {
            BoomBot.config.customCommands.add(command);
            BoomBot.configLoader.writeConfig();
            return true;
        }
        return false;
    }

    public void clearCustomCommands() {
        for (Command c : BoomBot.config.customCommands)
            commands.remove(c);
    }

    public boolean removeCommand(String name) {
        Iterator<Command> it = BoomBot.config.customCommands.listIterator();
        while (it.hasNext()) {
            Command c = it.next();
            if (c.getName().equalsIgnoreCase(name)) {
                it.remove();
                commands.remove(c);
                BoomBot.configLoader.writeConfig();
                return true;
            }
        }
        return false;
    }

    public boolean executeCommand(CommandInterface cmd) {
        for (Command c : commands) {
            if (c.getName().equalsIgnoreCase(cmd.getCommand())) {
                if (c.canExecuteCommand(cmd)) {
                    Logger.info("%s used %s command.", cmd.getUser().getUsername(), cmd.getCommand());
                    c.executeCommand(cmd);
                    return true;
                } else {
                    cmd.sendMessage("%s does not have enough permissions to use %s command!", cmd.getUser().getUsername(), cmd.getCommand());
                    return false;
                }
            }
        }
        return false;
    }
}
