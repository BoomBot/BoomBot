package net.lomeli.boombot.commands.special.audio.lib;

import org.json.JSONObject;

public class AudioInfo {
    private String title, url;
    private long length, size;

    public AudioInfo(JSONObject jsonObject) {
        this.title = jsonObject.getString("title");
        this.url = jsonObject.getString("link");
        this.length = parseString(jsonObject.getString("length"));
        this.size = parseString(jsonObject.getString("filesize"));
    }

    public long getLength() {
        return length;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    private static long parseString(String st) {
        try {
            return Long.parseLong(st);
        } catch (Exception ignore) {
            return 0;
        }
    }
}
