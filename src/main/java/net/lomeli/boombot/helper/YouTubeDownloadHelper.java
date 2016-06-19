package net.lomeli.boombot.helper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeDownloadHelper {
    public static Pattern pattern = Pattern.compile("(?:youtube\\.com\\/(?:[^\\/]+\\/.+\\/|(?:v|e(?:mbed)?)\\/|.*[?&]v=)|youtu\\.be\\/)([^\"&?\\/ ]{11})",
            Pattern.CASE_INSENSITIVE);

    // Because I'm too lazy/incompetent to improve the regex
    private static String clearURL(String url) {
        String clean = url;
        if (clean.contains("https://"))
            clean = clean.replace("https://", "");
        if (clean.contains("http://"))
            clean = clean.replace("http://", "");
        if (clean.contains("www."))
            clean = clean.replace("www.", "");
        return clean;
    }

    public static boolean isYouTubeVideo(String url) {
        url = clearURL(url);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    public static String extractYTId(String url) {
        url = clearURL(url);
        String vId = null;
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches())
            vId = matcher.group(1);
        return vId;
    }

    public static JSONObject getVideoInfo(String videoID) {
        String result;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("http://www.youtubeinmp3.com/fetch/?format=JSON&video=http://www.youtube.com/watch?v=" + videoID + "&filesize=1");
        HttpResponse response;
        try {
            response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = convertStreamToString(instream);
                return new JSONObject(result);
            }
        } catch (IOException ex) {
            Logger.error("An issue occurred trying to get a YT video with the id %s", ex, videoID);
        }
        return null;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
