package net.lomeli.boombot.commands.special.audio;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.entities.VoiceStatus;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;

public class JoinVoiceCommand extends Command {
    public JoinVoiceCommand() {
        super("voice-join", "BoomBot summoned to %s.");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        VoiceChannel channel = getUserVoiceChannel(cmd.getUser(), cmd.getGuild());
        VoiceChannel currentChannel = BoomBot.jda.getAudioManager(cmd.getGuild()).getConnectedChannel();
        if (channel == null) {
            cmd.sendMessage("Join a Voice Channel first before summoning me.");
            return;
        }
        if (!channel.checkPermission(BoomBot.jda.getSelfInfo(), Permission.VOICE_SPEAK)) {
            cmd.sendMessage("BoomBot cannot speak in %s.", channel.getName());
            return;
        }
        if (currentChannel != null && channel.getId().equals(currentChannel.getId())) {
            cmd.sendMessage("BoomBot already in %s.", channel.getName());
            return;
        }
        BoomBot.jda.getAudioManager(cmd.getGuild()).setSendingHandler(null);
        BoomBot.jda.getAudioManager(cmd.getGuild()).openAudioConnection(channel);
        cmd.sendMessage(getContent(), channel.getName());
    }

    private VoiceChannel getUserVoiceChannel(User user, Guild guild) {
        VoiceStatus status = guild.getVoiceStatusOfUser(user);
        return status != null ? status.getChannel() : null;
    }
}
