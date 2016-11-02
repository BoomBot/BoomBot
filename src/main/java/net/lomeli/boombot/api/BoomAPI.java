package net.lomeli.boombot.api;

import net.lomeli.boombot.api.commands.ICommandRegistry;
import net.lomeli.boombot.api.data.IDataRegistry;
import net.lomeli.boombot.api.events.IEventRegistry;
import net.lomeli.boombot.api.util.Logger;
import net.lomeli.boombot.api.util.lang.II18nRegistry;

public class BoomAPI {
    public static boolean debugMode = false;
    public static Logger logger = new Logger("BoomBot-API");

    public static IDataRegistry dataRegistry;
    public static II18nRegistry langRegistry;
    public static ICommandRegistry commandRegistry;
    public static IEventRegistry eventRegistry;
}
