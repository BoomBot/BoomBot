package net.lomeli.boombot.commands.special.audio.lib;

import net.dv8tion.jda.audio.player.FilePlayer;

public class AudioPlayer extends FilePlayer {
    public void closeStream() {
        if (audioSource != null) {
            try {
                audioSource.close();
            } catch (Exception ignored) {
            }
        }
        if (amplitudeAudioStream != null) {
            try {
                amplitudeAudioStream.close();
            } catch (Exception ignored) {
            }
        }
    }
}
