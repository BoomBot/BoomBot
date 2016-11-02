package net.lomeli.boombot.api.util;

import com.google.common.base.Strings;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.data.GuildData;
import net.lomeli.boombot.api.util.lang.I18n;

public class BasicGuildUtil {
    private static final String COMMAND_KEY = "commandKey";
    private static final String ALLOW_MENTION = "allowMentions";
    private static final String ALLOW_TTS = "allowTTS";
    private static final String ALLOW_EVERYONE = "allowEveryoneMentions";
    private static final String ALLOW_HERE = "allowHereMentions";
    private static final String COMMAND_DELAY = "disableClearChat";
    private static final String BANNED_USERS = "banUsers";
    private static final String GUILD_LANG = "lang";
    // DEFAULT VALUES
    public static final String DEFAULT_KEY = "!";
    public static final String DEFAULT_LANG = "en_US";

    public static I18n getGuildLang(GuildData data) {
        String langKey = data.getGuildData().getString(GUILD_LANG);
        if (!BoomAPI.langRegistry.hasLang(langKey)) langKey = DEFAULT_LANG;
        return BoomAPI.langRegistry.getLang(langKey);
    }

    public static void setGuildLang(GuildData data, String lang) {
        data.getGuildData().setString(GUILD_LANG, lang);
    }

    public static String getGuildCommandKey(GuildData data) {
        String key = data.getGuildData().getString(COMMAND_KEY);
        return Strings.isNullOrEmpty(key) ? DEFAULT_KEY : key;
    }

    public static void setGuildCommandKey(GuildData data, String key) {
        data.getGuildData().setString(COMMAND_KEY, key);
    }

    public static String[] getGuildBanFromCommand(GuildData data) {
        return data.getGuildData().getStringArray(BANNED_USERS);
    }

    public static void setGuildBanFromCommand(GuildData data, String... args) {
        if (args != null && args.length > 0) data.getGuildData().setStringArray(BANNED_USERS, args);
    }

    public static int getGuildCommandDelay(GuildData data) {
        return data.getGuildData().getInteger(COMMAND_DELAY);
    }

    public static void setGuildCommandDelay(GuildData data, int delay) {
        if (delay >= 0) data.getGuildData().setInteger(COMMAND_DELAY, delay);
    }

    public static boolean guildAllowBotTTS(GuildData data) {
        return data.getGuildData().getBoolean(ALLOW_TTS);
    }

    public static void setGuildAllowBotTTS(GuildData data, boolean tts) {
        data.getGuildData().setBoolean(ALLOW_TTS, tts);
    }

    public static boolean guildAllowBotMention(GuildData data) {
        return data.getGuildData().getBoolean(ALLOW_MENTION);
    }

    public static void setGuildAllowBotMention(GuildData data, boolean mention) {
        data.getGuildData().setBoolean(ALLOW_MENTION, mention);
    }

    public static boolean guildAllowEveryoneMention(GuildData data) {
        return data.getGuildData().getBoolean(ALLOW_EVERYONE);
    }

    public static void setGuildAllowEveryoneMention(GuildData data, boolean mention) {
        data.getGuildData().setBoolean(ALLOW_EVERYONE, mention);
    }

    public static boolean guildAllowHereMention(GuildData data) {
        return data.getGuildData().getBoolean(ALLOW_HERE);
    }

    public static void setGuildAllowHereMention(GuildData data, boolean mention) {
        data.getGuildData().setBoolean(ALLOW_HERE, mention);
    }
}
