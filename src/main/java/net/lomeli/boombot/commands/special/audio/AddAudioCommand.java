package net.lomeli.boombot.commands.special.audio;

import net.dv8tion.jda.entities.VoiceChannel;

import java.util.regex.Pattern;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.lib.CommandInterface;
import net.lomeli.boombot.helper.YouTubeDownloadHelper;

public class AddAudioCommand extends Command {
    private Pattern pattern;

    public AddAudioCommand() {
        super("add-audio", "");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        VoiceChannel channel = BoomBot.jda.getAudioManager().getConnectedChannel();
        if (channel == null) {
            cmd.sendMessage("BoomBot isn't connected to any channel.");
            return;
        }
        if (cmd.getArgs().size() >= 2) {
            String type = cmd.getArgs().get(0);
            if (type.equalsIgnoreCase("Y"))
                handleYouTube(cmd);
            else if (type.equalsIgnoreCase("S"))
                handleSoundCloud(cmd);
            else
                cmd.sendMessage("Which source? \"Y\" for Youtube or \"S\" for SoundCloud");
        } else {
            if (cmd.getArgs().size() == 1) {
                if (cmd.getArgs().get(0).equalsIgnoreCase("y") || cmd.getArgs().get(0).equalsIgnoreCase("s"))
                    cmd.sendMessage("What video?");
                else
                    cmd.sendMessage("Which source? \"Y\" for Youtube or \"S\" for SoundCloud");
            } else
                cmd.sendMessage("What video?");
        }
    }

    private void handleYouTube(CommandInterface cmd) {
        String url = cmd.getArgs().get(1);
        if (YouTubeDownloadHelper.isYouTubeVideo(url)) {
            String videoID = YouTubeDownloadHelper.extractYTId(url);
            String title = YouTubeDownloadHelper.getTitle(videoID);
            String downloadURL = YouTubeDownloadHelper.youtubeDownloadURL(videoID);
            AudioHandler.INSTANCE.addAudioToQueue(new AudioInfo(downloadURL, title));
            cmd.sendMessage("Added %s to the queue.", title);
        } else {
            // Youtube search
        }
    }

    private void handleSoundCloud(CommandInterface cmd) {

    }
}
