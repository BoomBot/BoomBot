package net.lomeli.boombot.core.addon;

import java.lang.reflect.InvocationTargetException;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.events.bot.InitEvent;
import net.lomeli.boombot.api.events.bot.PostInitEvent;
import net.lomeli.boombot.core.addon.discovery.AddonType;

public class DummyContainer extends AddonContainer {

    public DummyContainer() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        super(BoomBot.class, null, AddonType.JAR);
    }

    @Override
    public void loadResources() {
    }

    @Override
    public void preInitAddon() throws IllegalAccessException, InvocationTargetException {
    }

    @Override
    public void initAddon(InitEvent event) throws IllegalAccessException, InvocationTargetException {
    }

    @Override
    public void postInitAddon(PostInitEvent event) throws IllegalAccessException, InvocationTargetException {
    }
}
