package net.lomeli.boombot.lib;

import net.lomeli.boombot.api.BoomAPI;

public class ShutdownHook implements Runnable {
    @Override
    public void run() {
        BoomAPI.dataRegistry.writeGuildData();
    }
}
