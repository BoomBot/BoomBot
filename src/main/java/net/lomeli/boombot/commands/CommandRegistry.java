package net.lomeli.boombot.commands;

import com.google.common.collect.Lists;
import net.dv8tion.jda.Permission;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.event.EventRegistry;
import net.lomeli.boombot.api.event.text.CommandEvent;
import net.lomeli.boombot.commands.special.*;
import net.lomeli.boombot.commands.special.audio.AddAudioCommand;
import net.lomeli.boombot.commands.special.audio.JoinVoiceCommand;
import net.lomeli.boombot.commands.special.audio.LeaveVoiceCommand;
import net.lomeli.boombot.commands.special.create.ClearCommand;
import net.lomeli.boombot.commands.special.create.CreateCommand;
import net.lomeli.boombot.commands.special.create.RemoveCommand;
import net.lomeli.boombot.commands.special.moderate.*;
import net.lomeli.boombot.helper.Logger;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;

public enum CommandRegistry {
    INSTANCE();

    private List<Command> commands;
    private List<Command> addonCommands;

    CommandRegistry() {
        commands = Lists.newArrayList();
        addonCommands = Lists.newArrayList();
    }

    public void registerBasicCommands() {
        addNewCommand(new HelpCommand(), false);
        addNewCommand(new RunningCommand(), false);
        addNewCommand(new ReloadConfigCommand(), false);
        addNewCommand(new Command("about", "boombot.command.about"), false);
        addNewCommand(new StopBotCommand(), false);
        addNewCommand(new ClearChatCommand(), false);
        addNewCommand(new OptionsCommand(), false);
        addNewCommand(new GuildStatCommand(), false);
        addNewCommand(new AvatarCommand(), false);

        addNewCommand(new CreateCommand(), false);
        addNewCommand(new RemoveCommand(), false);
        addNewCommand(new ClearCommand(), false);
        addNewCommand(new BanUseCommand(), false);
        addNewCommand(new UnBanUseCommand(), false);
        addNewCommand(new UnrestrictCommand(), false);
        addNewCommand(new RestrictCommand(), false);

        addNewCommand(new KickCommand(), false);
        addNewCommand(new BanCommand(), false);

        if (BoomBot.debug) {
            Logger.info("Registering debugging and in-development commands");
            addNewCommand(new JoinVoiceCommand(), false);
            addNewCommand(new LeaveVoiceCommand(), false);
            addNewCommand(new AddAudioCommand(), false);

            //Debugging command
            addNewCommand(new GuildIdCommand(), false);
            addNewCommand(new ChannelIdCommand(), false);
        }
    }

    private boolean addNewCommand(Command command, boolean addon) {
        if (addon) {
            for (Command c : addonCommands) {
                if (c.getName().equalsIgnoreCase(command.getName())) {
                    Logger.info("Command with name \"%s\" already exists, ignoring...", command.getName());
                    return false;
                }
            }
            addonCommands.add(command);
        } else {
            for (Command c : commands) {
                if (c.getName().equalsIgnoreCase(command.getName())) {
                    Logger.info("Command with name \"%s\" already exists, ignoring...", command.getName());
                    return false;
                }
            }
            commands.add(command);
        }
        return true;
    }

    public boolean addNewCommand(Command command) {
        return addNewCommand(command, true);
    }

    public List<Command> getCommands() {
        return Lists.newArrayList(commands);
    }

    public List<Command> getAddonCommands() {
        return Lists.newArrayList(addonCommands);
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

        // Check Addon Commands
        for (Command c : addonCommands) {
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
        if (exCommand != null && !EventRegistry.INSTANCE.post(new CommandEvent.Pre(exCommand, cmd.getUser(), cmd.getGuild(), cmd.getChannel()))) {
            Logger.info("%s used %s command.", cmd.getUser().getUsername(), cmd.getCommand());
            exCommand.executeCommand(cmd);
            EventRegistry.INSTANCE.post(new CommandEvent.Post(exCommand, cmd.getUser(), cmd.getGuild(), cmd.getChannel()));
            return true;
        }
        return false;
    }
}
