package net.lomeli.boombot.commands;

import com.google.common.collect.Lists;
import net.dv8tion.jda.Permission;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.special.*;
import net.lomeli.boombot.commands.special.audio.AddAudioCommand;
import net.lomeli.boombot.commands.special.audio.JoinVoiceCommand;
import net.lomeli.boombot.commands.special.audio.LeaveVoiceCommand;
import net.lomeli.boombot.commands.special.create.*;
import net.lomeli.boombot.commands.special.moderate.*;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;
import net.lomeli.boombot.lib.Logger;

public enum CommandRegistry {
    INSTANCE();

    private List<Command> commands;

    CommandRegistry() {
        commands = Lists.newArrayList();
        registerBasicCommands();
    }

    private void registerBasicCommands() {
        addNewCommand(new HelpCommand());
        addNewCommand(new RunningCommand());
        addNewCommand(new ReloadConfigCommand());
        addNewCommand(new Command("about", "Hi, I'm BoomBot. I was made by @Lomeli12 as a fun little project.\nYou can find out more about me at https://github.com/BoomBot/BoomBot"));
        addNewCommand(new StopBotCommand());

        addNewCommand(new CreateCommand());
        addNewCommand(new RemoveCommand());
        addNewCommand(new ClearCommand());
        addNewCommand(new BanUseCommand());
        addNewCommand(new UnBanUseCommand());
        addNewCommand(new DerestrictCommand());
        addNewCommand(new RestrictCommand());

        addNewCommand(new KickCommand());
        addNewCommand(new BanCommand());

        addNewCommand(new JoinVoiceCommand());
        addNewCommand(new LeaveVoiceCommand());
        addNewCommand(new AddAudioCommand());

        //Debugging command
        addNewCommand(new GuildIdCommand());
        addNewCommand(new ChannelIdCommand());
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

    public List<Command> getCommands() {
        return commands;
    }

    public boolean executeCommand(CommandInterface cmd) {
        Command exCommand = null;
        GuildOptions guildOptions = BoomBot.config.getGuildOptions(cmd.getGuild());
        if (guildOptions.isUserBanned(cmd.getUser())) {
            cmd.sendUserMessage("You cannot use commands in %s.", cmd.getGuild().getName());
            return false;
        }
        if (guildOptions.isChannelRestricted(cmd.getChannel()) && !PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL)) {
            cmd.sendUserMessage("%s is in restricted use mode.", cmd.getChannel().getName());
            return false;
        }
        // Check Built-In commands
        for (Command c : commands) {
            if (c.getName().equalsIgnoreCase(cmd.getCommand())) {
                if (c.canExecuteCommand(cmd)) {
                    exCommand = c;
                    break;
                } else {
                    cmd.sendMessage("%s does not have enough permissions to use %s command!", cmd.getUser().getUsername(), cmd.getCommand());
                    return false;
                }
            }
        }
        // Check Guild Commands
        if (exCommand == null) {
            for (Command c : guildOptions.getCommandList()) {
                if (c.getName().equalsIgnoreCase(cmd.getCommand())) {
                    if (c.canExecuteCommand(cmd)) {
                        exCommand = c;
                        break;
                    } else {
                        cmd.sendMessage("%s does not have enough permissions to use %s command!", cmd.getUser().getUsername(), cmd.getCommand());
                        return false;
                    }
                }
            }
        }
        if (exCommand != null) {
            Logger.info("%s used %s command.", cmd.getUser().getUsername(), cmd.getCommand());
            exCommand.executeCommand(cmd);
            return true;
        }
        return false;
    }
}
