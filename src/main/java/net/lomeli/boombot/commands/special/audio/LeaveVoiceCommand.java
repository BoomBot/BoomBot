package net.lomeli.boombot.commands.special.audio;

import net.dv8tion.jda.entities.VoiceChannel;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class LeaveVoiceCommand extends Command {
    public LeaveVoiceCommand() {
        super("voice-leave", "BoomBot left %s.");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        VoiceChannel channel = BoomBot.jda.getAudioManager(cmd.getGuild()).getConnectedChannel();
        if (channel == null) {
            cmd.sendMessage("BoomBot isn't connected to any channel.");
            return;
        }
        cmd.sendMessage(getContent(), channel.getName());
        try {
            if (AudioHandler.INSTANCE.getPlayer() != null)
                AudioHandler.INSTANCE.getPlayer().stop();
            BoomBot.jda.getAudioManager(cmd.getGuild()).setSendingHandler(null);
            BoomBot.jda.getAudioManager(cmd.getGuild()).closeAudioConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
