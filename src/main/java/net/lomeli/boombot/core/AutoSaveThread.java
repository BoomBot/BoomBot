package net.lomeli.boombot.core;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.nbt.TagBase;
import net.lomeli.boombot.lib.DataKeys;

public class AutoSaveThread implements Runnable {
    private long lastTime;
    public static long SAVE_DELAY;

    public AutoSaveThread() {
        if (BoomAPI.dataRegistry.getBoomBotData().hasTag(DataKeys.AUTO_SAVE_DELAY, TagBase.TagType.TAG_LONG))
            SAVE_DELAY = BoomAPI.dataRegistry.getBoomBotData().getLong(DataKeys.AUTO_SAVE_DELAY);
        else SAVE_DELAY = 900000L;
    }

    @Override
    public void run() {
        while (true) {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= lastTime + SAVE_DELAY) {
                //BoomAPI.dataRegistry.writeData();
                lastTime = currentTime;
            }
        }
    }
}
