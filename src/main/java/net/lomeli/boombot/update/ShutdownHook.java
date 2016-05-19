package net.lomeli.boombot.update;

import net.lomeli.boombot.BoomBot;

public class ShutdownHook extends Thread {
    @Override
    public void run() {
        BoomBot.configLoader.writeConfig();
    }
}
