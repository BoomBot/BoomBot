package net.lomeli.boombot.commands.special.audio;

import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        cmd.getGuildOptions().getAudioHandler().stop();
    }
}
