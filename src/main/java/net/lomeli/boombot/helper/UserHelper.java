package net.lomeli.boombot.helper;

import com.google.common.base.Strings;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

import net.lomeli.boombot.BoomBot;

public class UserHelper {

    public static boolean areUsersTheSame(User main, User compare) {
        if (main == null || compare == null) return false;
        return main.getId().equals(compare.getId());
    }

    public static boolean isUserBoomBot(User user) {
        return areUsersTheSame(BoomBot.jda.getSelfInfo(), user);
    }

    public static boolean doesUserMatch(User user, String username, String discrimnator) {
        if (user == null || Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(discrimnator)) return false;
        return user.getUsername().equalsIgnoreCase(username) && user.getDiscriminator().equalsIgnoreCase(discrimnator);
    }

    public static boolean isOwner(User user, Guild guild) {
        return (guild == null && user == null) ? false : user.getId().equals(guild.getOwnerId());
    }

    public static User getUser(Guild guild, String id) {
        if (guild == null || Strings.isNullOrEmpty(id)) return null;
        for (User user : guild.getUsers()) {
            if (user != null && user.getId().equals(id))
                return user;
        }
        return null;
    }
}
