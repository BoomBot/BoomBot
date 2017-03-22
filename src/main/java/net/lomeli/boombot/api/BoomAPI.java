package net.lomeli.boombot.api;

import java.io.File;

import net.lomeli.boombot.api.permissions.IPermissionHelper;
import net.lomeli.boombot.api.handlers.IDataHandler;
import net.lomeli.boombot.api.registry.IEventRegistry;
import net.lomeli.boombot.api.util.Logger;
import net.lomeli.boombot.api.registry.II18nRegistry;

public class BoomAPI {
    public static boolean debugMode = false;
    public static int MAJOR, MINOR, REV;
    public static String BOOM_BOT_VERSION;
    public static Logger logger = new Logger("BoomBot-API");
    public static final File ADDON_FOLDER = new File("addons");

    public static IDataHandler dataRegistry;
    public static II18nRegistry langRegistry;
    public static IEventRegistry eventRegistry;
    public static IPermissionHelper permissionHelper;
}
