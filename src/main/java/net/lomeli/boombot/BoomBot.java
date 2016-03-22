package net.lomeli.boombot;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Date;

import net.lomeli.boombot.lang.LangRegistry;
import net.lomeli.boombot.lib.BoomConfig;
import net.lomeli.boombot.lib.Logger;

public class BoomBot {
    public static BoomListen listener;
    public static JDA jda;
    public static Date startTime;
    public static BoomConfig config;
    public static ConfigLoader configLoader;
    public static File logFolder, logFile;

    public static void main(String[] args) {
        try {
            LangRegistry.initRegistry();
            logFolder = new File("logs");
            if (!logFolder.exists())
                logFolder.mkdir();
            logFile = new File(logFolder, (new Date() + ".log").replaceAll(":", "-").replaceAll(" ", "_"));
            config = new BoomConfig();
            configLoader = new ConfigLoader(new File("config.cfg"));
            configLoader.parseConfig();
            if (args.length >= 2) {
                listener = new BoomListen();
                jda = new JDABuilder(args[0], args[1]).addListener(listener).buildBlocking();
                startTime = new Date();
                Logger.info("Bot is ready");
            } else {
                Logger.info("BoomBot requires a email and password to login as!");
            }
        } catch (IllegalArgumentException e) {
            Logger.error("The config was not populated. Please enter an email and password.", e);
        } catch (LoginException e) {
            Logger.error("The provided email / password combination was incorrect. Please provide valid details.", e);
        } catch (InterruptedException e) {
            Logger.error("An Exception occurred", e);
        }
    }
}
