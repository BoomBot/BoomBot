package net.lomeli.boombot.api;

import java.io.File;

import net.lomeli.boombot.api.registry.IDataRegistry;
import net.lomeli.boombot.api.registry.IEventRegistry;
import net.lomeli.boombot.api.util.Logger;
import net.lomeli.boombot.api.registry.II18nRegistry;

public class BoomAPI {
    public static boolean debugMode = false;
    public static int MAJOR, MINOR, REV;
    public static String BOOM_BOT_VERSION;
    public static Logger logger = new Logger("BoomBot-API");
    public static final File ADDON_FOLDER = new File("addons");

    public static IDataRegistry dataRegistry;
    public static II18nRegistry langRegistry;
    public static IEventRegistry eventRegistry;
}