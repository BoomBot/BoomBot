package net.lomeli.boombot.command.moderate;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.managers.GuildController;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.commands.CommandData;
import net.lomeli.boombot.api.commands.CommandResult;
import net.lomeli.boombot.api.commands.ICommand;
import net.lomeli.boombot.api.lib.I18n;
import net.lomeli.boombot.api.util.GuildUtil;
import net.lomeli.boombot.lib.util.PermissionUtil;

public class BanCommand implements ICommand {
    @Override
    public CommandResult execute(CommandData cmd) {
        if (cmd.getArgs().isEmpty() || cmd.getArgs().get(0).equalsIgnoreCase("?") ||
                cmd.getArgs().get(0).equalsIgnoreCase("help"))
            return new CommandResult("boombot.command.ban.help").setPrivateMessage(true);
        if (cmd.getMentionedUserIDs().isEmpty())
            return new CommandResult("boombot.command.ban.error.none").setPrivateMessage(true);
        if (cmd.getMentionedUserIDs().get(0).equals(cmd.getUserInfo().getUserID()))
            return new CommandResult("boombot.command.ban.error.self").setPrivateMessage(true);
        Guild guild = BoomBot.jda.getGuildById(cmd.getGuildID());
        GuildController controller = new GuildController(guild);
        Member owner = guild.getOwner();
        if (cmd.getMentionedUserIDs().get(0).equals(owner.getUser().getId()))
            return new CommandResult("boombot.command.ban.error.owner", owner.getEffectiveName()).setPrivateMessage(true);
        Member member = guild.getMemberById(cmd.getMentionedUserIDs().get(0));
        if (member == null) return new CommandResult("boombot.command.ban.error.none").setPrivateMessage(true);
        I18n lang = GuildUtil.getGuildLang(cmd.getGuildData());
        String msg = lang.getLocalization("boombot.command.ban.message", guild.getName());
        if (cmd.getArgs().size() > 1)
            msg += "\n" + cmd.getMessage().substring(("<@" + cmd.getMentionedUserIDs().get(0) + "> ").length());
        controller.ban(member, 0);
        if (!member.getUser().hasPrivateChannel()) member.getUser().openPrivateChannel().queue();
        member.getUser().getPrivateChannel().sendMessage(msg).queue();
        return new CommandResult("boombot.command.ban", member.getEffectiveName());
    }

    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public boolean canUserExecute(CommandData cmd) {
        return PermissionUtil.hasPermissions(cmd.getGuildID(), cmd.getUserInfo().getUserID(), Permission.BAN_MEMBERS);
    }

    @Override
    public boolean canBotExecute(CommandData cmd) {
        return PermissionUtil.boomBotHaverPermission(cmd.getGuildID(), Permission.BAN_MEMBERS);
    }

    @Override
    public CommandResult failedToExecuteMessage(CommandData cmd) {
        if (canUserExecute(cmd))
            return new CommandResult("boombot.command.ban.error.perm.self").setPrivateMessage(true);
        return new CommandResult("boombot.command.ban.error.perm.bot").setPrivateMessage(true);
    }
}
