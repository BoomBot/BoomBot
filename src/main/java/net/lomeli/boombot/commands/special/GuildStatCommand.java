package net.lomeli.boombot.commands.special;

import com.google.common.base.Strings;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.lib.GuildOptions;
import net.lomeli.boombot.lib.stats.GuildData;

public class GuildStatCommand extends Command {
    public GuildStatCommand() {
        super("stats", "boombot.command.stats");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        GuildOptions options = cmd.getGuildOptions();
        GuildData data = new GuildData(cmd.getGuild());
        String msg = "```";
        msg += options.translate(getContent(), data.getGuildName()) + "\n";
        if (!Strings.isNullOrEmpty(data.getOwnerName()))
            msg += options.translate(getContent() + ".owner", data.getGuildName(), data.getOwnerName()) + "\n";
        msg += options.translate(getContent() + ".users", data.getTotalUsers(), data.getOnlineUsers(), data.getAwayUsers(),
                data.getOfflineUsers()) + "\n";
        if (data.getTextChannels() > 0 && !Strings.isNullOrEmpty(data.getMainChannel()))
            msg += options.translate(getContent() + ".text", data.getTextChannels(), data.getMainChannel()) + "\n";
        if (data.getVoiceChannels() > 0)
            msg += options.translate(getContent() + ".voice", data.getVoiceChannels()) + "\n";
        msg += "```";
        cmd.sendMessage(msg);
    }
}
