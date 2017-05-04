package net.lomeli.boombot.api.events.user;

import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.lib.UserProxy;

public class UserEvent extends Event {
    private final UserProxy userProxy;
    private final String guildID;

    private UserEvent(UserProxy userProxy, String guildID) {
        this.userProxy = userProxy;
        this.guildID = guildID;
    }

    public UserProxy getUserProxy() {
        return userProxy;
    }

    public String getGuildID() {
        return guildID;
    }

    public static class UserJoinedEvent extends UserEvent {
        public UserJoinedEvent(UserProxy userProxy, String guildID) {
            super(userProxy, guildID);
        }
    }

    public static class UserLeaveEvent extends UserEvent {
        public UserLeaveEvent(UserProxy userProxy, String guildID) {
            super(userProxy, guildID);
        }
    }
}
