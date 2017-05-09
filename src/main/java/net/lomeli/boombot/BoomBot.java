package net.lomeli.boombot;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.impl.GameImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.List;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.events.bot.InitEvent;
import net.lomeli.boombot.api.events.bot.PostInitEvent;
import net.lomeli.boombot.api.events.registry.RegisterCommandEvent;
import net.lomeli.boombot.api.registry.ICommandRegistry;
import net.lomeli.boombot.api.util.Logger;
import net.lomeli.boombot.command.custom.CustomRegistry;
import net.lomeli.boombot.core.AutoSaveThread;
import net.lomeli.boombot.core.EventListener;
import net.lomeli.boombot.core.addon.Loader;
import net.lomeli.boombot.core.handler.DataHandler;
import net.lomeli.boombot.core.handler.MessageHandler;
import net.lomeli.boombot.core.registry.CommandRegistry;
import net.lomeli.boombot.core.registry.EventRegistry;
import net.lomeli.boombot.core.registry.I18nRegistry;
import net.lomeli.boombot.lib.ShutdownHook;
import net.lomeli.boombot.lib.util.PermissionHelper;

@Addon(addonID = "boombot", name = "BoomBot", version = BoomBot.BOOM_BOT_VERSION)
public class BoomBot {
    public static String debugGuildID;
    public static final int MAJOR = 3, MINOR = 0, REV = 0;
    public static final String BOOM_BOT_VERSION = MAJOR + "." + MINOR + "." + REV;
    public static Logger logger;
    public static JDA jda;
    public static EventListener mainListener;
    public static Loader addonLoader;
    public static ICommandRegistry commandRegistry;
    public static AutoSaveThread autoSaveThread;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook()));
        logger = new Logger("BoomBot");
        Logger.setupLogFolder();
        logger.info("Starting BoomBot v%s", BOOM_BOT_VERSION);
        addonLoader = new Loader();

        if (args != null && args.length > 0) {
            String key = args[0];

            if (args.length > 1) checkIfDebug(args);

            logger.info("Setting up registries");
            setupRegistry();

            addonLoader.loadAddons();

            logger.info("Setting up main listener");
            mainListener = new EventListener();

            try {
                jda = new JDABuilder(AccountType.BOT).setToken(key).addEventListener(mainListener).setBulkDeleteSplittingEnabled(false).buildBlocking();
                BoomAPI.dataRegistry.readData();
                new Thread(autoSaveThread = new AutoSaveThread()).start();

                InitEvent initEvent = new InitEvent(jda.getSelfUser().getId(), jda.getSelfUser().getName(), jda.getSelfUser().getDiscriminator());
                addonLoader.initAddons(initEvent);

                // Gets the guilds boombot is currently a member of
                List<String> guildIds = Lists.newArrayList();
                if (!jda.getGuilds().isEmpty())
                    jda.getGuilds().stream().filter(guild -> guild != null).forEach(guild -> guildIds.add(guild.getId()));
                String[] ids = new String[guildIds.size()];

                PostInitEvent postEvent = new PostInitEvent("JDA", guildIds.toArray(ids));
                addonLoader.postInitAddon(postEvent);

                BoomAPI.eventRegistry.post(new RegisterCommandEvent(commandRegistry));

                jda.getPresence().setGame(new GameImpl("JDA", "https://github.com/DV8FromTheWorld/JDA", Game.GameType.DEFAULT));
                logger.info("BoomBot finished loading!");
            } catch (LoginException ex) {
                logger.error("Could not login with given key: %s", ex, key);
            } catch (InterruptedException ex) {
                logger.error("JDA thread unexpectedly interrupted!", ex);
            } catch (RateLimitedException ex) {
                logger.error("BoomBot hit Discord's rate limit!", ex);
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
        BoomAPI.MAJOR = MAJOR;
        BoomAPI.MINOR = MINOR;
        BoomAPI.REV = REV;
        BoomAPI.BOOM_BOT_VERSION = String.format("%s.%s.%s", BoomAPI.MAJOR, BoomAPI.MINOR, BoomAPI.REV);
        BoomAPI.eventRegistry = new EventRegistry();
        commandRegistry = new CommandRegistry();
        BoomAPI.eventRegistry.registerEventHandler(CustomRegistry.INSTANCE);
        BoomAPI.langRegistry = new I18nRegistry();
        BoomAPI.dataRegistry = new DataHandler(new File("data"));
        BoomAPI.messageHandler = new MessageHandler();
        BoomAPI.permissionHelper = new PermissionHelper();
        if (!BoomAPI.debugMode)
            BoomAPI.langRegistry.loadLangFolder("boombot",
                    new File(BoomBot.class.getProtectionDomain().getCodeSource().getLocation().getPath()));
    }
}
