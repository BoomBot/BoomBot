package net.lomeli.boombot.api.events.bot.data;

import java.util.Map;

import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.nbt.TagCompound;

public class DataEvent extends Event {
    protected final Map<String, TagCompound> dataRegistry;
    protected final TagCompound boomBotData;

    private DataEvent(Map<String, TagCompound> dataRegistry, TagCompound boomBotData) {
        this.dataRegistry = dataRegistry;
        this.boomBotData = boomBotData;
    }

    public TagCompound getGuildData(String id) {
        return dataRegistry.get(id);
    }

    public String[] getGuildIDs() {
        return dataRegistry.keySet().toArray(new String[dataRegistry.keySet().size()]);
    }

    /**
     * Fires right before BoomBot saves guild data.
     */
    public static class DataWriteEvent extends DataEvent {
        public DataWriteEvent(Map<String, TagCompound> dataRegistry, TagCompound boomBotData) {
            super(dataRegistry, boomBotData);
        }

        public Map<String, TagCompound> getData() {
            return dataRegistry;
        }

        public TagCompound getBotData() {
            return boomBotData;
        }
    }

    /**
     * Fires right after BoomBot has read all guild data.
     */
    public static class DataReadEvent extends DataEvent {
        public DataReadEvent(Map<String, TagCompound> dataRegistry, TagCompound boomBotData) {
            super(dataRegistry, boomBotData);
        }

        /**
         * @return Unmodifiable Bot data
         */
        public TagCompound getBotData() {
            return boomBotData;
        }
    }
}
