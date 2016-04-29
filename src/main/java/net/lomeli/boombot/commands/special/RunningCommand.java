package net.lomeli.boombot.commands.special;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class RunningCommand extends Command {
    public RunningCommand() {
        super("running", "boombot.command.running");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        long diff = System.currentTimeMillis() - BoomBot.startTime.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        String msg = "```" + cmd.getGuildOptions().translate(getContent(), BoomBot.startTime) + "\n" + cmd.getGuildOptions().translate(getContent() + ".timesince", diffDays, diffHours, diffMinutes, diffSeconds);
        if (BoomBot.debug)
            msg += "\nI'm in debug mode, so I've probably not been up for very long.";
        msg += "```";
        cmd.sendMessage(msg);
    }
}
