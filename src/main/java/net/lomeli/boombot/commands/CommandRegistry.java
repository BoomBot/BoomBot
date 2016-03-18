package net.lomeli.boombot.commands;

import com.google.common.collect.Lists;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.special.*;
import net.lomeli.boombot.commands.special.create.ClearCommand;
import net.lomeli.boombot.commands.special.create.RemoveCommand;
import net.lomeli.boombot.commands.special.create.CreateCommand;
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
        addNewCommand(new ReloadConfigCommand());
        addNewCommand(new ClearCommand());
        addNewCommand(new BanCommand());
        addNewCommand(new Command("about", "Hi, I'm BoomBot. I was made by @Lomeli12 as a fun little project.\nYou can find out more about me at https://github.com/Lomeli12/BoomBot"));
        //Debugging command
        addNewCommand(new GuildIdCommand());
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

    public boolean executeCommand(CommandInterface cmd) {
        List<Command> fullList = Lists.newArrayList(commands);
        fullList.addAll(BoomBot.config.getCommandsForGuild(cmd.getGuild()));
        for (Command c : fullList) {
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
