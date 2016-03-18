package net.lomeli.boombot.commands.special;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.helper.PermissionsHelper;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.Logger;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban", "%s has been banned! Their messages from the past %s day(s) have been deleted. %s");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        if (cmd.getArgs().size() >= 2) {
            User user = getUser(cmd.getArgs().get(0), cmd.getGuild());
            int daysDelete = parseString(cmd.getArgs().get(1));
            if (user == null) {
                cmd.sendMessage("Cannot ban %s, as they DO NOT exist.", cmd.getArgs().get(0));
                return;
            }
            if (user.getId().equals(BoomBot.jda.getSelfInfo().getId())) {
                cmd.sendMessage("BoomBot probably shouldn't be banning itself...");
                return;
            }
            if (daysDelete <= -1) {
                cmd.sendMessage("Days worth of messages delete must be at least 0.");
                return;
            }
            String reason = "";
            if (cmd.getArgs().size() >= 3) {
                for (int i = 2; i < cmd.getArgs().size(); i++)
                    reason += cmd.getArgs().get(i) + " ";
            }
            if (!reason.isEmpty())
                reason = "Reason: " + reason;
            cmd.getGuild().getManager().ban(user, daysDelete);
            user.getPrivateChannel().sendMessage(String.format("You have been banned from %s. Your messages from the past %s day(s) have been deleted. %s", cmd.getGuild().getName(), daysDelete, reason));
            cmd.sendMessage(getContent(), user.getUsername(), daysDelete, reason);
        } else {
            if (cmd.getArgs().size() == 1)
                cmd.sendMessage("Please specify how many days back we go back to delete their messages. Must be at least 0.");
            else
                cmd.sendMessage("Ban who?");
        }
    }

    private User getUser(String name, Guild guild) {
        for (User user : guild.getUsers()) {
            if (user != null && user.getUsername().equalsIgnoreCase(name))
                return user;
        }
        return null;
    }

    private int parseString(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean canExecuteCommand(CommandInterface cmd) {
        if (!PermissionsHelper.hasPermissions(Permission.BAN_MEMBERS, cmd.getGuild())) {
            String s = "BoomBot does not have enough permissions to ban users. Please give BoomBot a role that can ban users to use this command.";
            Logger.info(s);
            cmd.sendMessage(s);
            return false;
        }
        return PermissionsHelper.userHasPermissions(cmd.getUser(), cmd.getGuild(), Permission.BAN_MEMBERS);
    }
}