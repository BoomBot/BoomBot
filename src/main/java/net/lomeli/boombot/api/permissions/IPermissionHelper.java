package net.lomeli.boombot.api.permissions;

public interface IPermissionHelper {
    /**
     * =============================
     * == Int to Permission Guide ==
     * =============================
     * CREATE_INSTANT_INVITE ----- 0
     * KICK_MEMBERS -------------- 1
     * BAN_MEMBERS --------------- 2
     * ADMINISTRATOR ------------- 3
     * MANAGE_CHANNEL ------------ 4
     * MANAGE_SERVER ------------- 5
     * MESSAGE_ADD_REACTION ------ 6
     * <p>
     * MESSAGE_READ -------------- 10
     * MESSAGE_WRITE ------------- 11
     * MESSAGE_TTS --------------- 12
     * MESSAGE_MANAGE ------------ 13
     * MESSAGE_EMBED_LINKS ------- 14
     * MESSAGE_ATTACH_FILES ------ 15
     * MESSAGE_HISTORY ----------- 16
     * MESSAGE_MENTION_EVERYONE -- 17
     * MESSAGE_EXT_EMOJI --------- 18
     * <p>
     * VOICE_CONNECT ------------- 20
     * VOICE_SPEAK --------------- 21
     * VOICE_MUTE_OTHERS --------- 22
     * VOICE_DEAF_OTHERS --------- 23
     * VOICE_MOVE_OTHERS --------- 24
     * VOICE_USE_VAD ------------- 25
     * <p>
     * NICKNAME_CHANGE ----------- 26
     * NICKNAME_MANAGE ----------- 27
     * <p>
     * MANAGE_ROLES -------------- 28
     * MANAGE_PERMISSIONS -------- 28
     * MANAGE_WEBHOOKS ----------- 29
     * MANAGE_EMOTES ------------- 30
     * <p>
     * UNKNOWN ------------------- -1
     **/

    boolean userHasPermission(String guildID, String userID, int... permisssionIDs);

    boolean boomBotHasPermission(String guildID, int... permissionIDs);
}
