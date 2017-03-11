package net.lomeli.boombot.command.moderate;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.RestAction;

import java.util.List;
import java.util.concurrent.ExecutionException;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.permissions.BotPermission;
import net.lomeli.boombot.lib.util.PermissionUtil;

public class ClearChatCommand implements ICommand {

    @Override
    public CommandResult execute(CommandData cmd) {
        if (cmd.getArgs().size() > 1)
            return new CommandResult("boombot.command.clear.error.onearg").setPrivateMessage(true);
        int i = Integer.parseInt(cmd.getArgs().get(0));
        if (cmd.getArgs().isEmpty() || i < 1)
            return new CommandResult("boombot.command.clear.error.specify").setPrivateMessage(true);
        if (i > 100) return new CommandResult("boombot.command.clear.error.much");
        if (i < 2) return new CommandResult("boombot.command.clear.error.few");
        Guild guild = BoomBot.jda.getGuildById(cmd.getGuildID());
        TextChannel channel = guild.getTextChannelById(cmd.getChannelID());
        RestAction<List<Message>> rest = channel.getHistory().retrievePast(i);
        try {
            List<Message> messages = rest.submit().get();
            if (messages == null || messages.isEmpty())
                return new CommandResult("boombot.command.clear.error.none").setPrivateMessage(true);
            if (messages.size() >= 2 && messages.size() <= 100)
                channel.deleteMessages(messages).queue();
        } catch (InterruptedException | ExecutionException ex) {
            BoomBot.logger.error("Could not clear %s messages", ex, i);
            return failedToExecuteMessage(cmd);
        }
        return new CommandResult("boombot.command.clear", i).setPrivateMessage(true);
    }


    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public boolean canUserExecute(CommandData cmd) {
        return BotPermission.userHavePermission(cmd.getUserInfo().getUserID(), cmd.getGuildID(), BotPermission.MODERATE);
    }

    @Override
    public boolean canBotExecute(CommandData cmd) {
        return PermissionUtil.boomBotHaverPermission(cmd.getGuildID(), Permission.MESSAGE_MANAGE);
    }

    @Override
    public CommandResult failedToExecuteMessage(CommandData cmd) {
        return new CommandResult("boombot.command.clear.error").setPrivateMessage(true);
    }
}
