package net.lomeli.boombot.update;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStreamReader;
import java.net.URL;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.helper.Logger;

public class UpdateUtil {
    private static UpdateJson remoteData;
    private static final String JSON_REMOTE = "";

    public static void getRemoteData() {
        try {
            Gson gson = new GsonBuilder().create();
            URL url = new URL(JSON_REMOTE);
            remoteData = gson.fromJson(new InputStreamReader(url.openStream()), UpdateJson.class);
        } catch(Exception e) {
            Logger.error("Failed to get remote data", e);
        }
    }

    public static boolean updateAvailable() {
        if (remoteData == null) getRemoteData();
        return compareVersion(remoteData);
    }

    public static String downloadURL() {
        if (remoteData == null) getRemoteData();
        return remoteData.getDownloadURL();
    }

    private static boolean compareVersion(UpdateJson update) {
        if (update == null) return false;
        if (BoomBot.MAJOR > update.getMajor()) return false;
        if (BoomBot.MAJOR < update.getMajor()) return true;
        if (BoomBot.MINOR > update.getMinor()) return false;
        if (BoomBot.MINOR < update.getMinor()) return true;
        if (BoomBot.REV > update.getRevision()) return false;
        if (BoomBot.REV < update.getRevision()) return true;
        return false;
    }
}
