package net.lomeli.boombot;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Date;

import net.lomeli.boombot.addons.Loader;
import net.lomeli.boombot.commands.CommandRegistry;
import net.lomeli.boombot.helper.Logger;
import net.lomeli.boombot.lang.LangRegistry;
import net.lomeli.boombot.lib.BoomConfig;
import net.lomeli.boombot.update.ShutdownHook;

public class BoomBot {
    public static final int MAJOR = 1, MINOR = 0, REV = 0;
    public static BoomListen listener;
    public static JDA jda;
    public static Date startTime;
    public static BoomConfig config;
    public static ConfigLoader configLoader;
    public static File logFolder, logFile;
    public static boolean debug;
    public static Loader addonLoader;

    public static void main(String[] args) {
        //TODO: Stop any calls to System.exit()
        addonLoader = new Loader();
        LangRegistry.initRegistry();
        if (debug)
            Logger.info("Adding Shutdown Hook");
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
        try {
            logFolder = new File("logs");
            if (!logFolder.exists())
                logFolder.mkdir();
            logFile = new File(logFolder, (new Date() + ".log").replaceAll(":", "-").replaceAll(" ", "_"));
            config = new BoomConfig();
            configLoader = new ConfigLoader(new File("config.cfg"));
            configLoader.parseConfig();
            if (args.length >= 1) {
                if (args.length > 1) {
                    for (int i = 1; i < args.length; i++) {
                        String arg = args[i];
                        switch (arg) {
                            case "-d":
                            case "--debug":
                                debug = true;
                                break;
                            default:
                                Logger.warn("Argument %s not recognized, ignoring.", arg);
                        }
                    }
                }
                listener = new BoomListen();
                jda = new JDABuilder().setBotToken(args[0]).addListener(listener).buildBlocking();
                startTime = new Date();
                jda.getAccountManager().setGame("BoomBot using JDA");
                Logger.info("Bot is ready");
                if (debug) {
                    jda.getAccountManager().setGame("BoomBot using JDA - Debug Mode");
                    Logger.info("BoomBot is in debug mode!");
                }
                CommandRegistry.INSTANCE.registerBasicCommands();
                addonLoader.loadAddons();
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
    
    public static void shutdownBoomBot() {
        configLoader.writeConfig();
        jda.shutdown();
    }
}
