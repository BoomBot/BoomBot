package net.lomeli.boombot.lib.util;

import com.google.common.base.Strings;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.utils.PermissionUtil;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.data.EntityData;
import net.lomeli.boombot.core.registry.DataRegistry;

public class BotPermissionUtil {
    public static boolean isUserBoomBotAdmin(String userID) {
        EntityData boomBotData = ((DataRegistry) BoomAPI.dataRegistry).getBoomBotData();
        if (boomBotData == null) return false;
        String[] adminIDs = boomBotData.getStringArray("adminIDs");
        if (adminIDs == null || adminIDs.length <= 0) return false;
        for (String id : adminIDs)
            if (!Strings.isNullOrEmpty(id) && id.equalsIgnoreCase(userID)) return true;
        return false;
    }

    public static boolean doesBoomBotHavePermissions(String guildID, Permission... permissions) {
        Guild guild = BoomBot.jda.getGuildById(guildID);
        if (guild == null) return false;
        return PermissionUtil.checkPermission(guild, BoomBot.jda.getSelfInfo(), permissions);
    }
}
