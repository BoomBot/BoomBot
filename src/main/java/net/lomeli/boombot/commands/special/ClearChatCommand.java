package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.ChannelHelper;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;

public class ClearChatCommand extends Command {

    public ClearChatCommand() {
        super("clear-chat", "");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        List<Message> messageList = ChannelHelper.getChannelMessages(cmd.getChannel(), cmd.getMessage());
        if (messageList != null && !messageList.isEmpty())
            messageList.stream().filter(m -> m != null).forEach(m -> m.deleteMessage());
        cmd.getMessage().deleteMessage();
    }

    @Override
    public boolean canUserExecute(CommandInterface cmd) {
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.MESSAGE_MANAGE);
    }

    @Override
    public boolean canBoomBotExecute(CommandInterface cmd) {
        return PermissionsHelper.hasPermissions(Permission.MESSAGE_MANAGE, cmd.getGuild());
    }

    @Override
    public String cannotExecuteMessage(UserType userType, CommandInterface cmd) {
        String permissionLang = "Message Management";
        return String.format("%s requires %s permissions to use %s",
                (userType.isBoomBot() ? BoomBot.jda.getSelfInfo().getUsername() : cmd.getUser().getUsername()),
                permissionLang, cmd.getCommand());
    }
}
