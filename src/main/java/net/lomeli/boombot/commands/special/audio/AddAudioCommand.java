package net.lomeli.boombot.commands.special.audio;

import com.google.common.base.Strings;
import net.dv8tion.jda.entities.VoiceChannel;
import org.json.JSONObject;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.commands.Command;
import net.lomeli.boombot.commands.special.audio.lib.AudioInfo;
import net.lomeli.boombot.helper.YouTubeDownloadHelper;
import net.lomeli.boombot.lib.CommandInterface;

public class AddAudioCommand extends Command {
    public AddAudioCommand() {
        super("add-audio", "");
    }

    @Override
    public void executeCommand(CommandInterface cmd) {
        VoiceChannel channel = BoomBot.jda.getAudioManager(cmd.getGuild()).getConnectedChannel();
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
            String downloadURL = "http://www.youtube.com/watch?v=" + videoID;
            try {
                JSONObject object = YouTubeDownloadHelper.getVideoInfo(videoID);
                String link = object.getString("link");
                if (Strings.isNullOrEmpty(link))
                    cmd.sendMessage("Video not found!");
                else {
                    String title = object.getString("title");
                    if (Strings.isNullOrEmpty(title))
                        title = downloadURL;
                    cmd.sendMessage("Added %s to audio queue", title);
                    cmd.getGuildOptions().getAudioHandler().addSong(new AudioInfo(object));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Youtube search
        }
    }

    private void handleSoundCloud(CommandInterface cmd) {

    }
}
