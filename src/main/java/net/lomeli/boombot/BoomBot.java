package net.lomeli.boombot;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.events.bot.InitEvent;
import net.lomeli.boombot.api.events.bot.PostInitEvent;
import net.lomeli.boombot.core.EventListner;
import net.lomeli.boombot.lib.CommandRegistry;
import net.lomeli.boombot.lib.DataRegistry;
import net.lomeli.boombot.lib.EventRegistry;

public class BoomBot {

    public static boolean debug;
    public static String debugGuildID;
    public static final int MAJOR = 3, MINOR = 0, REV = 0;
    public static final String BOOM_BOT_VERSION = String.format("%s.%s.%s", MAJOR, MINOR, REV);
    public static Logger logger;
    public static JDA jda;
    public static EventListner mainListener;

    public static void main(String[] args) {
        setupLogFolder();
        logger = LogManager.getLogger("BoomBot");
        logger.info("Starting BoomBot v{}", BOOM_BOT_VERSION);

        logger.info("Setting up registries");
        setupRegistry();

        if (args != null && args.length > 0) {
            String key = args[0];
            //TODO Search for addons and fire preInit events
            if (args.length > 1) checkIfDebug(args);
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
                    debug = true;
                    debugGuildID = splitArg[1];
                    logger.debug("BoomBot is now in debug mode using channel with id {}", debugGuildID);
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

    /**
     * Moves old logs to make room for latest log
     */
    public static void setupLogFolder() {
        File logFolder = new File("logs");
        if (!logFolder.exists()) logFolder.mkdir();
        File lastLog = new File(logFolder, "latest.log");
        if (lastLog.exists()) {
            try {
                Path logPath = Paths.get(lastLog.getCanonicalPath());
                BasicFileAttributes attrib = Files.readAttributes(logPath, BasicFileAttributes.class);
                FileTime time = attrib.creationTime();
                String name = String.format("%s.log", time.toInstant().toString().replace(":", "-"));
                FileUtils.copyFile(lastLog, new File(logFolder, name));
                lastLog.delete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
