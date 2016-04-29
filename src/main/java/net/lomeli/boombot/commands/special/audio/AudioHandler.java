package net.lomeli.boombot.commands.special.audio;

import com.google.common.collect.Lists;
import net.dv8tion.jda.audio.player.FilePlayer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.helper.YouTubeDownloadHelper;

public enum AudioHandler {
    INSTANCE();

    private List<AudioInfo> fileList;
    private File audioFile;
    private FilePlayer player;

    AudioHandler() {
        fileList = Lists.newArrayList();
        audioFile = new File("play.mp3");
    }

    private void downloadNextFile() {
        if (!fileList.isEmpty()) {
            try {
                String url = fileList.get(0).getDownloadUrl();
                if (audioFile.exists())
                    audioFile.delete();
                URL website = new URL(url);
                if (YouTubeDownloadHelper.isYouTubeVideo(url)){
                    //VGet v = new VGet(website, audioFile);
                    //v.download();
                } else {
                    FileUtils.copyURLToFile(website, audioFile);
                }
                fileList.remove(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addAudioToQueue(AudioInfo audioInfo) {
        fileList.add(audioInfo);
        if (player == null || player.isStopped())
            play();
    }

    public boolean play() {
        if (fileList.isEmpty()) return false;
        else {
            try {
                if (player != null) {
                    if (player.isPaused()) {
                        player.play();
                        return true;
                    }
                    if (player.isStopped()) {
                        player.restart();
                        return true;
                    }
                    return false;
                } else {
                    if (audioFile.exists()) {
                        player = new FilePlayer(audioFile);
                        //BoomBot.jda.getAudioManager().setSendingHandler(player);
                        player.play();
                        return true;
                    } else {
                        synchronized (this) {
                            downloadNextFile();
                        }
                        player = new FilePlayer(audioFile);
                        //BoomBot.jda.getAudioManager().setSendingHandler(player);
                        player.play();
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                player.stop();
                player = null;
                //BoomBot.jda.getAudioManager().setAudioConnection(null);
            }
        }
        return false;
    }

    public boolean pause() {
        if (player == null || player.isPaused()) return false;
        player.pause();
        return true;
    }

    public FilePlayer getPlayer() {
        return player;
    }
}
