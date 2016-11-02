package net.lomeli.boombot.lib.util;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
    private static final String EVERYONE = "@everyone";
    private static final String HERE = "@here";
    private static final String TTS = "/tts ";
    private static final Pattern MENTION_PATTER = Pattern.compile("<@((\\d){1,32})>");

    /**
     * Removes any mentioned users already in a string
     */
    public static String stripMentions(String raw, Guild guild) {
        String out = raw;
        Matcher match = MENTION_PATTER.matcher(raw);
        while (match.find()) {
            String group = match.group();
            String id = group.substring(2, group.length() - 1);
            User user = guild.getUserById(id);
            if (user != null)
                out = out.replaceAll(group, "@" + user.getUsername());
        }
        return out;
    }

    public static String stripEveryoneMention(String raw) {
        return raw.replaceAll("(?i)" + EVERYONE, "everyone");
    }

    public static String stripHere(String raw) {
        return raw.replaceAll("(?i)" + HERE, "here");
    }

    public static String stripTTS(String raw) {
        return raw.replaceAll("(?i)" + TTS, "");
    }
}
