package net.lomeli.boombot.command.admin;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.permissions.BotPermission;
import net.lomeli.boombot.lib.util.PermissionUtil;

public class SetAdminCommand implements ICommand {
    @Override
    public CommandResult execute(CommandData cmd) {
        List<String> mentionedUsers = cmd.getMentionedUserIDs();
        String username = cmd.getUserInfo().hasNickName() ? cmd.getUserInfo().getNickName() : cmd.getUserInfo().getUserName();
        if (mentionedUsers.isEmpty()) {
            if (PermissionUtil.addUserAsAdmin(cmd.getUserInfo().getUserID()))
                return new CommandResult("boombot.command.setadmin", username);
            return new CommandResult("boombot.command.setadmin.error.admin", username);
        } else if (mentionedUsers.size() > 1 || cmd.getArgs().size() > 1)
            return new CommandResult("boombot.command.setadmin.error.multiple");
        else {
            Guild guild = BoomBot.jda.getGuildById(cmd.getGuildID());
            Member user = guild.getMemberById(mentionedUsers.get(0));
            if (user == null) return new CommandResult("boombot.command.setadmin.error.nouser");
            if (PermissionUtil.addUserAsAdmin(user.getUser().getId()))
                return new CommandResult("boombot.command.setadmin", username);
            return new CommandResult("boombot.command.setadmin.error.admin", username);
        }
    }

    @Override
    public String getName() {
        return "setadmin";
    }

    @Override
    public boolean canUserExecute(CommandData cmd) {
        return !PermissionUtil.isBotAdminSet() || BotPermission.isUserBoomBotAdmin(cmd.getUserInfo().getUserID());
    }

    @Override
    public boolean canBotExecute(CommandData cmd) {
        return canUserExecute(cmd);
    }

    @Override
    public CommandResult failedToExecuteMessage(CommandData cmd) {
        return new CommandResult("boombot.command.setadmin.error.set");
    }
}
