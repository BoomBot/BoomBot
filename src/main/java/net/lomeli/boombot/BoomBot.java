package net.lomeli.boombot;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.List;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.events.bot.InitEvent;
import net.lomeli.boombot.api.events.bot.PostInitEvent;
import net.lomeli.boombot.api.util.Logger;
import net.lomeli.boombot.command.custom.CustomRegistry;
import net.lomeli.boombot.core.EventListner;
import net.lomeli.boombot.core.registry.CommandRegistry;
import net.lomeli.boombot.core.registry.DataRegistry;
import net.lomeli.boombot.core.registry.EventRegistry;
import net.lomeli.boombot.lib.ShutdownHook;

public class BoomBot {

    public static String debugGuildID;
    public static final int MAJOR = 3, MINOR = 0, REV = 0;
    public static final String BOOM_BOT_VERSION = String.format("%s.%s.%s", MAJOR, MINOR, REV);
    public static Logger logger;
    public static JDA jda;
    public static EventListner mainListener;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));
        Logger.setupLogFolder();
        logger = new Logger("BoomBot");
        logger.info("Starting BoomBot v%s", BOOM_BOT_VERSION);

        if (args != null && args.length > 0) {
            String key = args[0];
            //TODO Search for addons and fire preInit events
            if (args.length > 1) checkIfDebug(args);

            logger.info("Setting up registries");
            setupRegistry();

            logger.info("Setting up main listener");
            mainListener = new EventListner();

            try {
                jda = new JDABuilder().setBotToken(key).addListener(mainListener).setBulkDeleteSplittingEnabled(false).buildBlocking();
                InitEvent initEvent = new InitEvent(jda.getSelfInfo().getId(), jda.getSelfInfo().getUsername(), jda.getSelfInfo().getDiscriminator());
                //TODO Fire init event
                //TODO Load builtin commands
                // Gets the guilds boombot is currently a member of
                List<String> guildIds = Lists.newArrayList();
                if (!jda.getGuilds().isEmpty())
                    jda.getGuilds().stream().filter(guild -> guild != null).forEach(guild -> guildIds.add(guild.getId()));
                String[] ids = new String[guildIds.size()];
                PostInitEvent postEvent = new PostInitEvent("JDA", guildIds.toArray(ids));
                //TODO Fire post init event
                jda.getAccountManager().setGame(postEvent.getCurrentGame());
                logger.info("BoomBot finished loading!");
            } catch (LoginException ex) {
                logger.error("Could not login with given key: %s", key);
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                logger.error("JDA thread unexpectedly interrupted!");
                ex.printStackTrace();
            }
        }
    }

    private static void checkIfDebug(String[] args) {
        for (String arg : args) {
            if (!Strings.isNullOrEmpty(arg) && (arg.startsWith("-d=") || arg.startsWith("--debug="))) {
                String[] splitArg = arg.split("=");
                if (splitArg != null && splitArg.length == 2) {
                    BoomAPI.debugMode = true;
                    debugGuildID = splitArg[1];
                    logger.debug("BoomBot is now in debug mode using channel with id %s", debugGuildID);
                    return;
                }
            }
        }
    }

    private static void setupRegistry() {
        BoomAPI.eventRegistry = new EventRegistry();
        BoomAPI.commandRegistry = new CommandRegistry();
        BoomAPI.eventRegistry.registerEventHandler(CustomRegistry.INSTANCE);
        BoomAPI.dataRegistry = new DataRegistry(new File("data"));
        BoomAPI.dataRegistry.readGuildData();
    }
}
