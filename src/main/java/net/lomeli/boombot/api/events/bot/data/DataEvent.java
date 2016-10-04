package net.lomeli.boombot.api.events.bot.data;

import com.google.common.collect.Maps;

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

    public static class DataWriteEvent extends DataEvent {

        public DataWriteEvent(Map<String, GuildData> dataRegistry) {
            super(dataRegistry);
        }

        public void setGuildData(String id, GuildData data) {
            dataRegistry.put(id, data);
        }

        public Map<String, GuildData> getData() {
            return Maps.newHashMap(dataRegistry);
        }
    }

    public static class DataReadEvent extends DataEvent {

        public DataReadEvent(Map<String, GuildData> dataRegistry) {
            super(dataRegistry);
        }

        public String[] getGuildIDs() {
            return dataRegistry.keySet().toArray(new String[dataRegistry.keySet().size()]);
        }
    }
}
