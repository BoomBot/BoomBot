package net.lomeli.boombot.commands;

import com.google.common.collect.Lists;
import net.dv8tion.jda.Permission;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.special.*;
import net.lomeli.boombot.commands.special.audio.AddAudioCommand;
import net.lomeli.boombot.commands.special.audio.JoinVoiceCommand;
import net.lomeli.boombot.commands.special.audio.LeaveVoiceCommand;
import net.lomeli.boombot.commands.special.create.ClearCommand;
import net.lomeli.boombot.commands.special.create.CreateCommand;
import net.lomeli.boombot.commands.special.create.RemoveCommand;
import net.lomeli.boombot.commands.special.moderate.*;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;
import net.lomeli.boombot.helper.Logger;

public enum CommandRegistry {
    INSTANCE();

    private List<Command> commands;

    CommandRegistry() {
        commands = Lists.newArrayList();
    }

    public void registerBasicCommands() {
        addNewCommand(new HelpCommand());
        addNewCommand(new RunningCommand());
        addNewCommand(new ReloadConfigCommand());
        addNewCommand(new Command("about", "boombot.command.about"));
        addNewCommand(new StopBotCommand());
        addNewCommand(new ClearChatCommand());
        addNewCommand(new OptionsCommand());
        addNewCommand(new GuildStatCommand());

        addNewCommand(new CreateCommand());
        addNewCommand(new RemoveCommand());
        addNewCommand(new ClearCommand());
        addNewCommand(new BanUseCommand());
        addNewCommand(new UnBanUseCommand());
        addNewCommand(new UnrestrictCommand());
        addNewCommand(new RestrictCommand());

        addNewCommand(new KickCommand());
        addNewCommand(new BanCommand());

        if (BoomBot.debug) {
            Logger.info("Registering debugging and in-development commands");
            addNewCommand(new JoinVoiceCommand());
            addNewCommand(new LeaveVoiceCommand());
            addNewCommand(new AddAudioCommand());

            //Debugging command
            addNewCommand(new GuildIdCommand());
            addNewCommand(new ChannelIdCommand());
        }
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
        GuildOptions guildOptions = cmd.getGuildOptions();
        if (guildOptions.isUserBanned(cmd.getUser())) {
            cmd.sendUserMessage("boombot.command.banuse.message.banned", cmd.getGuild().getName());
            return false;
        }
        if (guildOptions.isChannelRestricted(cmd.getChannel()) && !PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MANAGE_CHANNEL)) {
            cmd.sendUserMessage("boombot.command.restrict", cmd.getChannel().getName());
            return false;
        }
        // Check Built-In commands
        for (Command c : commands) {
            if (c.getName().equalsIgnoreCase(cmd.getCommand())) {
                if (c.canUserExecute(cmd)) {
                    if (c.canBoomBotExecute(cmd))
                        exCommand = c;
                    else {
                        cmd.sendMessage(c.cannotExecuteMessage(Command.UserType.BOOMBOT, cmd));
                        return false;
                    }
                    break;
                } else {
                    cmd.sendMessage(c.cannotExecuteMessage(Command.UserType.USER, cmd));
                    return false;
                }
            }
        }
        // Check Guild Commands
        if (exCommand == null) {
            Command com = guildOptions.getCommand(cmd);
            if (com != null)
                exCommand = com;
        }
        if (exCommand != null) {
            Logger.info("%s used %s command.", cmd.getUser().getUsername(), cmd.getCommand());
            exCommand.executeCommand(cmd);
            return true;
        }
        return false;
    }
}
