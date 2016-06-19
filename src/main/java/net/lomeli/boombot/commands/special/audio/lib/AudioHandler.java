package net.lomeli.boombot.commands.special.audio.lib;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.lomeli.boombot.helper.Logger;
import net.lomeli.boombot.helper.ObfUtil;

public class AudioHandler implements Runnable {
    private AudioPlayer player;
    private List<AudioInfo> songQueue;
    private boolean isRunning;
    private final File songFile;
    private Thread downloaderThread;

    public AudioHandler() {
        this.player = new AudioPlayer();
        this.songQueue = Lists.newArrayList();
        this.songFile = new File("song.mp3");
        this.songFile.setWritable(true);
        this.songFile.setReadable(true);
        this.songFile.setExecutable(true);
        this.isRunning = true;
    }

    public void addSong(AudioInfo audioInfo) {
        songQueue.add(audioInfo);
        if (isStopped())
            downloadFile();
    }

    public void play() {
        if (this.isPaused())
            this.player.play();
        else if (this.isStopped())
            this.getPlayer().restart();
        else
            this.player.pause();
    }

    public void pause() {
        this.player.pause();
    }

    public boolean isPaused() {
        return this.player.isPaused();
    }

    public boolean isPlaying() {
        return this.player.isPlaying();
    }

    public boolean isStopped() {
        return this.player.isStopped();
    }

    public void stop() {
        this.songQueue.clear();
        this.player.stop();
        this.player.closeStream();
        try {
            if (this.songFile.exists())
                FileUtils.forceDelete(this.songFile);
        } catch (IOException ex) {
            Logger.error("Couldn't delete song.mp3", ex);
        }
    }

    public void next() {
        this.player.stop();
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    private void downloadFile() {
        if (songQueue.isEmpty()) return;
        AudioInfo file = null;
        try {
            if (songFile.exists()) {
                this.player.closeStream();
                FileUtils.forceDelete(songFile);
            }
            file = songQueue.get(0);
            URL url = new URL(file.getUrl());
            this.downloaderThread = new Thread(new FileDownloader(songFile, url, file.getSize()));
            this.downloaderThread.start();
        } catch (MalformedURLException ex) {
            Logger.error("Malformed URL in song queue", ex);
        } catch (IOException ex) {
            Logger.error("Could not open file %s", ex, file);
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            if (this.downloaderThread != null) {
                Runnable run = getThreadRunnable(this.downloaderThread);
                if (run instanceof FileDownloader && ((FileDownloader) run).isFinished()) {
                    Logger.debug("Attempting to play song file!");
                    try {
                        this.player.setAudioFile(songFile);
                        this.player.play();
                    } catch (IOException ex) {
                        Logger.error("Could not open song file", ex);
                    } catch (UnsupportedAudioFileException ex) {
                        Logger.error("Could not play song file", ex);
                    }
                    this.downloaderThread = null;
                }
            }
            if (this.isStopped() && songQueue.isEmpty() && songFile.exists()) {
                this.player.closeStream();
                songFile.delete();
            }
        }
    }

    private static Runnable getThreadRunnable(Thread thread) {
        return ObfUtil.getFieldValue(Thread.class, thread, "target");
    }
}
