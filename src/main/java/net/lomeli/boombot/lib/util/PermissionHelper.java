package net.lomeli.boombot.lib.util;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.permissions.IPermissionHelper;

public class PermissionHelper implements IPermissionHelper {

    @Override
    public boolean userHasPermission(String guildID, String userID, int... permissionIDs) {
        if (permissionIDs != null && permissionIDs.length <= 0) return false;
        Guild guild = BoomBot.jda.getGuildById(guildID);
        if (guild != null) {
            Member member = guild.getMemberById(userID);
            if (member != null) {
                Permission[] perms = new Permission[permissionIDs.length];
                for (int i = 0; i < perms.length; i++) perms[i] = Permission.getFromOffset(permissionIDs[i]);
                return member.hasPermission(perms);
            }
        }
        return false;
    }

    @Override
    public boolean boomBotHasPermission(String guildID, int... permissionIDs) {
        return userHasPermission(guildID, BoomBot.jda.getSelfUser().getId(), permissionIDs);
    }
}
