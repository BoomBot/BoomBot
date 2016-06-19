package net.lomeli.boombot.commands.special.audio;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class NextCommand extends Command {
    public NextCommand() {
        super("next", "");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        cmd.getGuildOptions().getAudioHandler().next();
    }
}
