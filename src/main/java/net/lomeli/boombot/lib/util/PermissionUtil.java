package net.lomeli.boombot.lib.util;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.nbt.NBTTagBase;
import net.lomeli.boombot.api.nbt.NBTTagCompound;
import net.lomeli.boombot.api.nbt.NBTTagList;
import net.lomeli.boombot.api.nbt.NBTTagString;

public class PermissionUtil {
    public static boolean isBotAdminSet() {
        NBTTagCompound boomBotData = BoomAPI.dataRegistry.getBoomBotData();
        if (boomBotData == null) return false;
        NBTTagBase adminIDs = boomBotData.getTag("adminIDs");
        if (adminIDs != null && adminIDs instanceof NBTTagList) {
            NBTTagList list = (NBTTagList) adminIDs;
            return list.getTagCount() > 0;
        }
        return false;
    }

    public static boolean boomBotHaverPermission(String guildID, Permission...permissions) {
        Guild guild = BoomBot.jda.getGuildById(guildID);
        Member member = null;
        if (guild != null) member = guild.getMemberById(BoomBot.jda.getSelfUser().getId());
        return member != null ? member.hasPermission(permissions) : false;
    }

    public static boolean addUserAsAdmin(String userId) {
        NBTTagCompound boomBotData = BoomAPI.dataRegistry.getBoomBotData();
        if (boomBotData == null) return false;
        NBTTagBase adminTag = boomBotData.getTag("adminIDs");
        NBTTagList adminList = new NBTTagList(NBTTagBase.TagType.TAG_STRING);
        if (adminTag instanceof NBTTagList && ((NBTTagList) adminTag).getType() == NBTTagBase.TagType.TAG_STRING)
            adminList = (NBTTagList) adminTag;
        boolean isAdmin = adminList.getValue().stream().filter(tag -> tag != null && tag.getValue() == userId).findAny().isPresent();
        if (!isAdmin) {
            adminList.add(new NBTTagString(userId));
            boomBotData.setTag("adminIDs", adminList);
            return true;
        }
        return false;
    }
}
