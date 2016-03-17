package net.lomeli.boombot.commands.special;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class RunningCommand extends Command {
    public RunningCommand() {
        super("running", "");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        cmd.sendMessage("BoomBot has been running since %s", BoomBot.startTime);
        long diff = System.currentTimeMillis() - BoomBot.startTime.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        cmd.sendMessage("It has been %s days, %s hours, %s minutes, and %s seconds since then!", diffDays, diffHours, diffMinutes, diffSeconds);
    }
}
