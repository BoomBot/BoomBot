package net.lomeli.boombot.api.events.bot;

import java.util.List;

import net.lomeli.boombot.api.events.Event;

public class GuildEvent extends Event {
    private final List<String> guildIDs;

    private GuildEvent(List<String> guildIDs) {
        this.guildIDs = guildIDs;
    }

    public List<String> getGuildIDs() {
        return guildIDs;
    }

    /**
     * Fires when BoomBot joins a new guild.
     */
    public static class JoinedGuildEvent extends GuildEvent {

        public JoinedGuildEvent(List<String> guildIDs) {
            super(guildIDs);
        }
    }

    /**
     * Fires when BoomBot leaves a guild.
     */
    public static class LeaveGuildEvent extends GuildEvent {

        public LeaveGuildEvent(List<String> guildIDs) {
            super(guildIDs);
        }
    }
}
