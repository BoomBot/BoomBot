package net.lomeli.boombot.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.lomeli.boombot.api.commands.ICommandRegistry;
import net.lomeli.boombot.api.data.IDataRegistry;
import net.lomeli.boombot.api.util.lang.II18nRegistry;

public class BoomAPI {
    public static Logger logger = LogManager.getLogger("BoomBot-API");

    public static IDataRegistry dataRegistry;
    public static II18nRegistry langRegistry;
    public static ICommandRegistry commandRegistry;
}
