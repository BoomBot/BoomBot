package net.lomeli.boombot;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Date;

import net.lomeli.boombot.lib.BoomConfig;
import net.lomeli.boombot.lib.Logger;

public class BoomBot {
    public static BoomListen listener;
    public static JDA jda;
    public static boolean run = true;
    public static Date startTime;
    public static BoomConfig config;
    public static ConfigLoader configLoader;

    public static void main(String[] args) {
        try {
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
            System.out.println("The config was not populated. Please enter an email and password.");
        } catch (LoginException e) {
            System.out.println("The provided email / password combination was incorrect. Please provide valid details.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        configLoader.writeConfig();
    }
}
