package net.lomeli.boombot.api.events.bot.data;

import java.util.Map;

import net.lomeli.boombot.api.data.GuildData;
import net.lomeli.boombot.api.events.Event;

public class DataEvent extends Event {
    protected final Map<String, GuildData> dataRegistry;

    private DataEvent(Map<String, GuildData> dataRegistry) {
        this.dataRegistry = dataRegistry;
    }

    public GuildData getGuildData(String id) {
        return dataRegistry.get(id);
    }

    public String[] getGuildIDs() {
        return dataRegistry.keySet().toArray(new String[dataRegistry.keySet().size()]);
    }

    /**
     * Fires right before BoomBot saves guild data.
     */
    public static class DataWriteEvent extends DataEvent {
        public DataWriteEvent(Map<String, GuildData> dataRegistry) {
            super(dataRegistry);
        }

        public Map<String, GuildData> getData() {
            return dataRegistry;
        }
    }

    /**
     * Fires right after BoomBot has read all guild data.
     */
    public static class DataReadEvent extends DataEvent {
        public DataReadEvent(Map<String, GuildData> dataRegistry) {
            super(dataRegistry);
        }
    }
}
