package net.lomeli.boombot.lib;

import net.lomeli.boombot.BoomBot;

public class ShutdownHook extends Thread {
    @Override
    public void run() {
        BoomBot.wrapUp();
    }
}
