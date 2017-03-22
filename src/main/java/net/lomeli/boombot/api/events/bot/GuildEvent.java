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

        private String guildID;

        public JoinedGuildEvent(List<String> guildIDs, String guildID) {
            super(guildIDs);
            this.guildID = guildID;
        }

        public String getNewGuildID() {
            return guildID;
        }
    }

    /**
     * Fires when BoomBot leaves a guild.
     */
    public static class LeaveGuildEvent extends GuildEvent {

        private String guildID;

        public LeaveGuildEvent(List<String> guildIDs, String guildID) {
            super(guildIDs);
            this.guildID = guildID;
        }

        public String getLeftGuildID() {
            return guildID;
        }
    }
}
