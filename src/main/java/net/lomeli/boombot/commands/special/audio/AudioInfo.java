package net.lomeli.boombot.commands.special.audio;

public class AudioInfo {
    private String downloadUrl;
    private String title;

    public AudioInfo(String downloadUrl, String title) {
        this.downloadUrl = downloadUrl;
        this.title = title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getTitle() {
        return title;
    }
}
