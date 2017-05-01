package net.lomeli.boombot.api.util;

import com.google.common.base.Strings;

import java.util.Optional;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.lib.I18n;
import net.lomeli.boombot.api.nbt.TagBase;
import net.lomeli.boombot.api.nbt.TagCompound;
import net.lomeli.boombot.api.nbt.TagList;

public class GuildUtil {
    private static final String COMMAND_KEY = "commandKey";
    private static final String ALLOW_MENTION = "allowMentions";
    private static final String ALLOW_TTS = "allowTTS";
    private static final String ALLOW_EVERYONE = "allowEveryoneMentions";
    private static final String ALLOW_HERE = "allowHereMentions";
    private static final String COMMAND_DELAY = "disableClearChat";
    private static final String GUILD_LANG = "lang";
    private static final String OWNER_ID = "ownerID";
    private static final String USER_DATA = "userData";
    private static final String USER_ID = "userID";
    private static final String TEXT_CHANNEL_DATA = "textChannelData";
    private static final String VOICE_CHANNEL_DATA = "voiceChannelData";
    // DEFAULT VALUES
    public static final String DEFAULT_KEY = "!?";
    public static final String DEFAULT_LANG = "en_US";

    public static TagCompound getGuildData(String id) {
        return BoomAPI.dataRegistry.getDataForGuild(id);
    }

    public static boolean isUserGuildOwner(String userID, String guildID) {
        TagCompound data = getGuildData(guildID);
        String ownerID = data.getString(OWNER_ID);
        return (data != null && !Strings.isNullOrEmpty(ownerID)) ? ownerID.equalsIgnoreCase(userID) : false;
    }

    public static void setGuildOwner(TagCompound data, String userid) {
        data.setString(OWNER_ID, userid);
    }

    public static TagCompound getGuildMemberData(TagCompound data, String userID) {
        TagBase tag = data.getTag(USER_DATA);
        TagList userData = new TagList(TagBase.TagType.TAG_COMPOUND);
        if (tag != null && tag instanceof TagList && ((TagList) tag).getType() == TagBase.TagType.TAG_COMPOUND)
            userData = (TagList) tag;
        Optional<TagBase> result = Optional.ofNullable(userData.stream().filter(nbt -> ((TagCompound) nbt).getString(USER_ID).equals(userID)).findFirst().orElse(null));
        TagCompound userInfo = (result != null && result.isPresent()) ? (TagCompound) result.get() : null;
        if (userInfo == null) {
            userInfo = new TagCompound();
            userInfo.setString(USER_ID, userID);
        }
        return userInfo;
    }

    public static TagCompound getGuildMemberData(String guildID, String userID) {
        return getGuildMemberData(getGuildData(guildID), userID);
    }

    public static void setGuildMemberData(TagCompound data, TagCompound userInfo) {
        TagBase tag = data.getTag(USER_DATA);
        TagList userData = new TagList(TagBase.TagType.TAG_COMPOUND);
        if (tag != null && tag instanceof TagList && ((TagList) tag).getType() == TagBase.TagType.TAG_COMPOUND)
            userData = (TagList) tag;
        TagCompound old = (TagCompound) userData.stream().filter(nbt -> ((TagCompound) nbt).getString(USER_ID).equals(userInfo.getString(USER_ID)))
                .findAny().get();
        userData.remove(old);
        userData.add(userInfo);
        data.setTag(USER_DATA, userData);
    }

    public static void setGuildMemberData(String guildID, TagCompound userInfo) {
        setGuildMemberData(getGuildData(guildID), userInfo);
    }

    public static I18n getGuildLang(String id) {
        return getGuildLang(getGuildData(id));
    }

    public static I18n getGuildLang(TagCompound data) {
        String langKey = data.getString(GUILD_LANG);
        if (!BoomAPI.langRegistry.hasLang(langKey)) langKey = DEFAULT_LANG;
        return BoomAPI.langRegistry.getLang(langKey);
    }

    public static void setGuildLang(String id, String lang) {
        setGuildLang(getGuildData(id), lang);
    }

    public static void setGuildLang(TagCompound data, String lang) {
        data.setString(GUILD_LANG, lang);
    }

    public static String getGuildCommandKey(String id) {
        return getGuildCommandKey(getGuildData(id));
    }

    public static String getGuildCommandKey(TagCompound data) {
        String key = data.getString(COMMAND_KEY);
        return Strings.isNullOrEmpty(key) ? DEFAULT_KEY : key;
    }

    public static void setGuildCommandKey(String id, String key) {
        setGuildCommandKey(getGuildData(id), key);
    }

    public static void setGuildCommandKey(TagCompound data, String key) {
        data.setString(COMMAND_KEY, key);
    }

    public static int getGuildCommandDelay(TagCompound data) {
        return data.getInt(COMMAND_DELAY);
    }

    public static void setGuildCommandDelay(TagCompound data, int delay) {
        if (delay >= 0) data.setInt(COMMAND_DELAY, delay);
    }

    public static boolean guildAllowBotTTS(TagCompound data) {
        return data.getInt(ALLOW_TTS) == 1;
    }

    public static void setGuildAllowBotTTS(TagCompound data, boolean tts) {
        data.setInt(ALLOW_TTS, tts ? 1 : 0);
    }

    public static boolean guildAllowBotMention(TagCompound data) {
        return data.getInt(ALLOW_MENTION) == 1;
    }

    public static void setGuildAllowBotMention(TagCompound data, boolean mention) {
        data.setInt(ALLOW_MENTION, mention ? 1 : 0);
    }

    public static boolean guildAllowEveryoneMention(TagCompound data) {
        return data.getInt(ALLOW_EVERYONE) == 1;
    }

    public static void setGuildAllowEveryoneMention(TagCompound data, boolean mention) {
        data.setInt(ALLOW_EVERYONE, mention ? 1 : 0);
    }

    public static boolean guildAllowHereMention(TagCompound data) {
        return data.getInt(ALLOW_HERE) == 1;
    }

    public static void setGuildAllowHereMention(TagCompound data, boolean mention) {
        data.setInt(ALLOW_HERE, mention ? 1 : 0);
    }
}
