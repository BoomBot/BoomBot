package net.lomeli.boombot.logging;

import net.lomeli.boombot.BoomBot;

public class LogThread implements Runnable {
    private long lastWrote;

    public LogThread() {
        lastWrote = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (true) {
            // Write every 5 minutes
            long currentTime = System.currentTimeMillis();
            if (currentTime >= lastWrote + 300000) {
                Logger.writeLogFile(BoomBot.logFolder, BoomBot.logFile);
                lastWrote = currentTime;
            }
        }
    }
}
