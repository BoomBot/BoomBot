package net.lomeli.boombot.lib.util;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.nbt.TagBase;
import net.lomeli.boombot.api.nbt.TagCompound;
import net.lomeli.boombot.api.nbt.TagList;
import net.lomeli.boombot.api.nbt.TagString;
import net.lomeli.boombot.lib.DataKeys;

public class PermissionUtil {
    public static boolean isBotAdminSet() {
        TagCompound boomBotData = BoomAPI.dataRegistry.getBoomBotData();
        if (boomBotData == null) return false;
        TagBase adminIDs = boomBotData.getTag(DataKeys.ADMIN_IDS);
        if (adminIDs != null && adminIDs instanceof TagList) {
            TagList list = (TagList) adminIDs;
            return list.getTagCount() > 0;
        }
        return false;
    }

    public static boolean hasPermissions(String guildID, String userID, Permission... permissions) {
        Guild guild = BoomBot.jda.getGuildById(guildID);
        if (guild != null) {
            Member member = guild.getMemberById(userID);
            if (member != null) return member.hasPermission(permissions);
        }
        return false;
    }

    public static boolean boomBotHaverPermission(String guildID, Permission... permissions) {
        return hasPermissions(guildID, BoomBot.jda.getSelfUser().getId(), permissions);
    }

    public static boolean addUserAsAdmin(String userId) {
        TagCompound boomBotData = BoomAPI.dataRegistry.getBoomBotData();
        if (boomBotData == null) return false;
        TagBase adminTag = boomBotData.getTag(DataKeys.ADMIN_IDS);
        TagList adminList = new TagList(TagBase.TagType.TAG_STRING);
        if (adminTag instanceof TagList && ((TagList) adminTag).getType() == TagBase.TagType.TAG_STRING)
            adminList = (TagList) adminTag;
        boolean isAdmin = adminList.getValue().stream().filter(tag -> tag != null && tag.getValue() == userId).findAny().isPresent();
        if (!isAdmin) {
            adminList.add(new TagString(userId));
            boomBotData.setTag(DataKeys.ADMIN_IDS, adminList);
            return true;
        }
        return false;
    }
}
