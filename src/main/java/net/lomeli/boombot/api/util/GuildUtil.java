package net.lomeli.boombot.api.util;

import com.google.common.base.Strings;

import java.util.Optional;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.lib.I18n;
import net.lomeli.boombot.api.nbt.NBTTagBase;
import net.lomeli.boombot.api.nbt.NBTTagCompound;
import net.lomeli.boombot.api.nbt.NBTTagList;

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

    public static NBTTagCompound getGuildData(String id) {
        return BoomAPI.dataRegistry.getDataForGuild(id);
    }

    public static boolean isUserGuildOwner(String userID, String guildID) {
        NBTTagCompound data = getGuildData(guildID);
        String ownerID = data.getString(OWNER_ID);
        return (data != null && !Strings.isNullOrEmpty(ownerID)) ?  ownerID.equalsIgnoreCase(userID) : false;
    }

    public static void setGuildOwner(NBTTagCompound data, String userid) {
        data.setString(OWNER_ID, userid);
    }

    public static NBTTagCompound getGuildMemberData(NBTTagCompound data, String userID) {
        NBTTagBase tag = data.getTag(USER_DATA);
        NBTTagList userData = new NBTTagList(NBTTagBase.TagType.TAG_COMPOUND);
        if (tag != null && tag instanceof NBTTagList && ((NBTTagList) tag).getType() == NBTTagBase.TagType.TAG_COMPOUND)
            userData = (NBTTagList) tag;
        Optional<NBTTagBase> result = userData.stream().filter(nbt -> ((NBTTagCompound) nbt).getString(USER_ID).equals(userID)).findFirst();
        NBTTagCompound userInfo = (NBTTagCompound) result.get();
        if (userInfo == null) {
            userInfo = new NBTTagCompound();
            userInfo.setString(USER_ID, userID);
        }
        return userInfo;
    }

    public static NBTTagCompound getGuildMemberData(String guildID, String userID) {
        return getGuildMemberData(getGuildData(guildID), userID);
    }

    public static void setGuildMemberData(NBTTagCompound data, NBTTagCompound userInfo) {
        NBTTagBase tag = data.getTag(USER_DATA);
        NBTTagList userData = new NBTTagList(NBTTagBase.TagType.TAG_COMPOUND);
        if (tag != null && tag instanceof NBTTagList && ((NBTTagList) tag).getType() == NBTTagBase.TagType.TAG_COMPOUND)
            userData = (NBTTagList) tag;
        NBTTagCompound old = (NBTTagCompound) userData.stream().filter(nbt -> ((NBTTagCompound) nbt).getString(USER_ID).equals(userInfo.getString(USER_ID)))
                .findAny().get();
        userData.remove(old);
        userData.add(userInfo);
        data.setTag(USER_DATA, userData);
    }

    public static void setGuildMemberData(String guildID, NBTTagCompound userInfo) {
        setGuildMemberData(getGuildData(guildID), userInfo);
    }

    public static I18n getGuildLang(String id) {
        return getGuildLang(getGuildData(id));
    }

    public static I18n getGuildLang(NBTTagCompound data) {
        String langKey = data.getString(GUILD_LANG);
        if (!BoomAPI.langRegistry.hasLang(langKey)) langKey = DEFAULT_LANG;
        return BoomAPI.langRegistry.getLang(langKey);
    }

    public static void setGuildLang(String id, String lang) {
        setGuildLang(getGuildData(id), lang);
    }

    public static void setGuildLang(NBTTagCompound data, String lang) {
        data.setString(GUILD_LANG, lang);
    }

    public static String getGuildCommandKey(String id) {
        return getGuildCommandKey(getGuildData(id));
    }

    public static String getGuildCommandKey(NBTTagCompound data) {
        String key = data.getString(COMMAND_KEY);
        return Strings.isNullOrEmpty(key) ? DEFAULT_KEY : key;
    }

    public static void setGuildCommandKey(String id, String key) {
        setGuildCommandKey(getGuildData(id), key);
    }

    public static void setGuildCommandKey(NBTTagCompound data, String key) {
        data.setString(COMMAND_KEY, key);
    }

    public static int getGuildCommandDelay(NBTTagCompound data) {
        return data.getInt(COMMAND_DELAY);
    }

    public static void setGuildCommandDelay(NBTTagCompound data, int delay) {
        if (delay >= 0) data.setInt(COMMAND_DELAY, delay);
    }

    public static boolean guildAllowBotTTS(NBTTagCompound data) {
        return data.getInt(ALLOW_TTS) == 1;
    }

    public static void setGuildAllowBotTTS(NBTTagCompound data, boolean tts) {
        data.setInt(ALLOW_TTS, tts ? 1 : 0);
    }

    public static boolean guildAllowBotMention(NBTTagCompound data) {
        return data.getInt(ALLOW_MENTION) == 1;
    }

    public static void setGuildAllowBotMention(NBTTagCompound data, boolean mention) {
        data.setInt(ALLOW_MENTION, mention ? 1 : 0);
    }

    public static boolean guildAllowEveryoneMention(NBTTagCompound data) {
        return data.getInt(ALLOW_EVERYONE) == 1;
    }

    public static void setGuildAllowEveryoneMention(NBTTagCompound data, boolean mention) {
        data.setInt(ALLOW_EVERYONE, mention ? 1 : 0);
    }

    public static boolean guildAllowHereMention(NBTTagCompound data) {
        return data.getInt(ALLOW_HERE) == 1;
    }

    public static void setGuildAllowHereMention(NBTTagCompound data, boolean mention) {
        data.setInt(ALLOW_HERE, mention ? 1 : 0);
    }
}
