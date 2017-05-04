package net.lomeli.boombot.api.permissions;
/**
 * <center>=============================<p>
 * == Int to Permission Guide ==<p>
 * =============================<p></center>
 * <li>
 * <ul>CREATE_INSTANT_INVITE ----- 0</ul>
 * <ul>KICK_MEMBERS -------------- 1</ul>
 * <ul>BAN_MEMBERS --------------- 2</ul>
 * <ul>ADMINISTRATOR ------------- 3</ul>
 * <ul>MANAGE_CHANNEL ------------ 4</ul>
 * <ul>MANAGE_SERVER ------------- 5</ul>
 * <ul>MESSAGE_ADD_REACTION ------ 6</ul>
 * <ul>MESSAGE_READ -------------- 10</ul>
 * <ul>MESSAGE_WRITE ------------- 11</ul>
 * <ul>MESSAGE_TTS --------------- 12</ul>
 * <ul>MESSAGE_MANAGE ------------ 13</ul>
 * <ul>MESSAGE_EMBED_LINKS ------- 14</ul>
 * <ul>MESSAGE_ATTACH_FILES ------ 15</ul>
 * <ul>MESSAGE_HISTORY ----------- 16</ul>
 * <ul>MESSAGE_MENTION_EVERYONE -- 17</ul>
 * <ul>MESSAGE_EXT_EMOJI --------- 18</ul>
 * <ul>VOICE_CONNECT ------------- 20</ul>
 * <ul>VOICE_SPEAK --------------- 21</ul>
 * <ul>VOICE_MUTE_OTHERS --------- 22</ul>
 * <ul>VOICE_DEAF_OTHERS --------- 23</ul>
 * <ul>VOICE_MOVE_OTHERS --------- 24</ul>
 * <ul>VOICE_USE_VAD ------------- 25</ul>
 * <ul>NICKNAME_CHANGE ----------- 26</ul>
 * <ul>NICKNAME_MANAGE ----------- 27</ul>
 * <ul>MANAGE_ROLES -------------- 28</ul>
 * <ul>MANAGE_PERMISSIONS -------- 28</ul>
 * <ul>MANAGE_WEBHOOKS ----------- 29</ul>
 * <ul>MANAGE_EMOTES ------------- 30</ul>
 * <ul>UNKNOWN ------------------- -1</ul>
 * </li>
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
