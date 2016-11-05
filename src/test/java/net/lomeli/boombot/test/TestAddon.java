package net.lomeli.boombot.test;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.events.bot.InitEvent;
import net.lomeli.boombot.api.events.bot.PostInitEvent;
import net.lomeli.boombot.api.events.bot.PreInitEvent;
import net.lomeli.boombot.api.events.text.MessageEvent;
import net.lomeli.boombot.api.util.Logger;

@Addon(addonID = "test_addon", name = "Test Addon", version = "1.0.0")
public class TestAddon {
    @Addon.Instance
    public static TestAddon INSTANCE;

    public static Logger logger;

    @Addon.Event
    public void preInit(PreInitEvent event) {
        logger = new Logger("Test Addon");
        logger.debug("Test addon loaded");
        logger.debug("Pre Event");
        BoomAPI.eventRegistry.registerEventHandler(INSTANCE);
    }

    @Addon.Event
    public void init(InitEvent event) {
        logger.debug("Init Event");
    }

    @Addon.Event
    public void postInit(PostInitEvent event){
        logger.debug("Post Event");
    }

    @Event.EventHandler
    public void testMessageEvent(MessageEvent event) {
        logger.debug("%s: %s", event.getUserID(), event.getMessage());
    }
}
