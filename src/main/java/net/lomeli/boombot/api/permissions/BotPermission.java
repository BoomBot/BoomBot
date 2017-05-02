package net.lomeli.boombot.api.permissions;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.nbt.TagBase;
import net.lomeli.boombot.api.nbt.TagCompound;
import net.lomeli.boombot.api.nbt.TagList;
import net.lomeli.boombot.api.nbt.TagString;
import net.lomeli.boombot.api.util.GuildUtil;

public enum BotPermission {
    /**
     * USE_BOT = Can use commands
     * MODERATE = Can moderate use of bot and remove/give USE_BOT permission.
     * CUSTOM_COMMANDS = Can make custom commands
     * GUILD_ADMIN = Can use all previous permissions (for quick assigning of power). Guild owners will be treated as though they have this permission.
     */
    USE_BOT, MODERATE, CUSTOM_COMMANDS, GUILD_ADMIN, NULL;
    private static final BotPermission[] VALID_ITEMS = {USE_BOT, MODERATE, CUSTOM_COMMANDS, GUILD_ADMIN, NULL};
    private static final String PERMISSION_KEY = "USER_PERMISSIONS";

    BotPermission() {
    }

    public static BotPermission getPermissionByID(int id) {
        return (id >= 0 && id < VALID_ITEMS.length) ? VALID_ITEMS[id] : NULL;
    }

    /**
     * Get all permissions a user has in a guild
     *
     * @param userID
     * @param guildID
     * @return an unmodifiable list of permissions a user has for given guild
     */
    public static Collection<BotPermission> getUserPermissions(String userID, String guildID) {
        List<BotPermission> permissions = Lists.newArrayList();
        TagCompound userData = GuildUtil.getGuildMemberData(guildID, userID);
        int[] permissionData = userData.getIntArray(PERMISSION_KEY);
        if (permissionData != null && permissionData.length > 0) {
            for (int p : permissionData) {
                BotPermission per = getPermissionByID(p);
                if (per != null) permissions.add(per);
            }
        }
        if (permissions.isEmpty()) {
            if (GuildUtil.isUserGuildOwner(userID, guildID)) {
                permissions.add(GUILD_ADMIN);
                if (userData != null) userData.setIntArray(PERMISSION_KEY, new int[]{3});
            } else {
                permissions.add(USE_BOT);
                if (userData != null) userData.setIntArray(PERMISSION_KEY, new int[]{0});
            }
            BoomAPI.dataRegistry.writeData();
        }
        return Collections.unmodifiableCollection(permissions);
    }

    /**
     * Add one or more permissions to a user.
     *
     * @param userID
     * @param guildID
     * @param permissions
     * @return
     */
    public static boolean addPermission(String userID, String guildID, BotPermission... permissions) {
        if (permissions == null || permissions.length < 1 || Strings.isNullOrEmpty(userID) || Strings.isNullOrEmpty(guildID))
            return false;
        TagCompound data = GuildUtil.getGuildMemberData(guildID, userID);
        List<BotPermission> perList = Lists.newArrayList(permissions);
        if (!data.hasTag(PERMISSION_KEY, TagBase.TagType.TAG_INT_ARRAY)) {
            BotPermission extraPerm = GuildUtil.isUserGuildOwner(userID, guildID) ? GUILD_ADMIN : USE_BOT;
            if (!perList.contains(extraPerm)) perList.add(extraPerm);
        }
        int[] perKey = new int[perList.size()];
        for (int i = 0; i < perKey.length; i++) perKey[i] = perList.get(i).ordinal();
        data.setIntArray(PERMISSION_KEY, perKey);
        BoomAPI.dataRegistry.writeData();
        return true;
    }

    /**
     * Check if user has permissions in this guild
     *
     * @param userID
     * @param guildID
     * @param permission
     * @return true of user has permissions or is guild owner.
     */
    public static boolean userHavePermission(String userID, String guildID, BotPermission permission) {
        if (GuildUtil.isUserGuildOwner(userID, guildID)) return true;
        return getUserPermissions(userID, guildID).contains(permission);
    }


    /**
     * USe this to check if user is an overall BoomBot admin, as opposed to guild admin.
     *
     * @param userID
     * @return
     */
    public static boolean isUserBoomBotAdmin(String userID) {
        TagCompound boomBotData = BoomAPI.dataRegistry.getBoomBotData();
        if (boomBotData == null) return false;
        TagBase adminIDs = boomBotData.getTag("adminIDs");
        if (adminIDs != null && adminIDs instanceof TagList) {
            TagList list = (TagList) adminIDs;
            if (list.getType() != TagBase.TagType.TAG_STRING || list.getTagCount() < 1) return false;
            return list.stream().filter(tag -> tag instanceof TagString)
                    .anyMatch(tag -> ((TagString) tag).getValue().equalsIgnoreCase(userID));
        }
        return false;
    }
}
