package net.lomeli.boombot;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import javax.security.auth.login.LoginException;

import java.util.Date;

import net.lomeli.boombot.lib.Logger;

public class BoomBot {
    public static BoomListen listener;
    public static JDA jda;
    public static boolean run = true;
    public static Date startTime;

    public static void main(String[] args) {
        try {
            if (args.length >= 2) {
                listener = new BoomListen();
                jda = new JDABuilder(args[0], args[1]).addListener(listener).buildBlocking();
                startTime = new Date();
                Logger.info("Bot is ready");
                while (run) {
                    if (listener.coolDown <= listener.MAX_COOL)
                        listener.coolDown++;
                }
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
        Logger.info("BoomBot shutting down...");
    }
}
