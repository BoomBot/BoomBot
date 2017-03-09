package net.lomeli.boombot.api.events.bot.data;

import java.util.Map;

import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.nbt.NBTTagCompound;

public class DataEvent extends Event {
    protected final Map<String, NBTTagCompound> dataRegistry;
    protected final NBTTagCompound boomBotData;

    private DataEvent(Map<String, NBTTagCompound> dataRegistry, NBTTagCompound boomBotData) {
        this.dataRegistry = dataRegistry;
        this.boomBotData = boomBotData;
    }

    public NBTTagCompound getGuildData(String id) {
        return dataRegistry.get(id);
    }

    public String[] getGuildIDs() {
        return dataRegistry.keySet().toArray(new String[dataRegistry.keySet().size()]);
    }

    /**
     * Fires right before BoomBot saves guild data.
     */
    public static class DataWriteEvent extends DataEvent {
        public DataWriteEvent(Map<String, NBTTagCompound> dataRegistry, NBTTagCompound boomBotData) {
            super(dataRegistry, boomBotData);
        }

        public Map<String, NBTTagCompound> getData() {
            return dataRegistry;
        }

        public NBTTagCompound getBotData() {
            return boomBotData;
        }
    }

    /**
     * Fires right after BoomBot has read all guild data.
     */
    public static class DataReadEvent extends DataEvent {
        public DataReadEvent(Map<String, NBTTagCompound> dataRegistry, NBTTagCompound boomBotData) {
            super(dataRegistry, boomBotData);
        }

        /**
         * @return Unmodifiable Bot data
         */
        public NBTTagCompound getBotData() {
           return boomBotData;
        }
    }
}
