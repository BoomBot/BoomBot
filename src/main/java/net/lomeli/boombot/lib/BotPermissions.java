package net.lomeli.boombot.lib;

import com.google.common.collect.Maps;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.lomeli.boombot.BoomBot;

public class BotPermissions {
    private static HashMap<String, List<Permission>> permissionCache = Maps.newHashMap();

    public static boolean hasPermissions(Permission permission, Guild guild) {
        if (permissionCache.containsKey(guild.getId()))
            return permissionCache.get(guild.getId()).contains(permission);
        else {
            List<Role> botRoles = guild.getRolesForUser(BoomBot.jda.getSelfInfo());
            if (botRoles != null && botRoles.size() > 0) {
                List<Permission> botPermissions = new ArrayList<>();
                botRoles.stream().filter(r -> r != null && r.getPermissions() != null).forEach(r -> botPermissions.addAll(r.getPermissions()));
                permissionCache.put(guild.getId(), botPermissions);
                return botPermissions.contains(permission);
            }
            return false;
        }
    }
}
