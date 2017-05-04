package net.lomeli.boombot.api.permissions;

/**
 * ===================
 * <p>== Int to Permission Guide ==</p>
 * <p>===================</p>
 * <p>CREATE_INSTANT_INVITE ----- 0</p>
 * <p>KICK_MEMBERS -------------- 1</p>
 * <p>BAN_MEMBERS --------------- 2</p>
 * <p>ADMINISTRATOR ------------- 3</p>
 * <p>MANAGE_CHANNEL ------------ 4</p>
 * <p>MANAGE_SERVER ------------- 5</p>
 * <p>MESSAGE_ADD_REACTION ------ 6</p>
 * <p>MESSAGE_READ -------------- 10</p>
 * <p>MESSAGE_WRITE ------------- 11</p>
 * <p>MESSAGE_TTS --------------- 12</p>
 * <p>MESSAGE_MANAGE ------------ 13</p>
 * <p>MESSAGE_EMBED_LINKS ------- 14</p>
 * <p>MESSAGE_ATTACH_FILES ------ 15</p>
 * <p>MESSAGE_HISTORY ----------- 16</p>
 * <p>MESSAGE_MENTION_EVERYONE -- 17</p>
 * <p>MESSAGE_EXT_EMOJI --------- 18</p>
 * <p>VOICE_CONNECT ------------- 20</p>
 * <p>VOICE_SPEAK --------------- 21</p>
 * <p>VOICE_MUTE_OTHERS --------- 22</p>
 * <p>VOICE_DEAF_OTHERS --------- 23</p>
 * <p>VOICE_MOVE_OTHERS --------- 24</p>
 * <p>VOICE_USE_VAD ------------- 25</p>
 * <p>NICKNAME_CHANGE ----------- 26</p>
 * <p>NICKNAME_MANAGE ----------- 27</p>
 * <p>MANAGE_ROLES -------------- 28</p>
 * <p>MANAGE_PERMISSIONS -------- 28</p>
 * <p>MANAGE_WEBHOOKS ----------- 29</p>
 * <p>MANAGE_EMOTES ------------- 30</p>
 * <p>UNKNOWN ------------------- -1</p>
 **/
public interface IPermissionHelper {
    /**
     * @param guildID
     * @param userID
     * @param permissionIDs
     * @return true if user has the specified permissions on the guild
     */
    boolean userHasPermission(String guildID, String userID, int... permissionIDs);

    /**
     * @param guildID
     * @param permissionIDs
     * @return true if BoomBot has the specified permissions on the guild
     */
    boolean boomBotHasPermission(String guildID, int... permissionIDs);
}
