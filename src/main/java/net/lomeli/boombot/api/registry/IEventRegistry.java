package net.lomeli.boombot.api.registry;

import net.lomeli.boombot.api.events.Event;

public interface IEventRegistry {
    void registerEventHandler(Object obj);

    boolean post(Event event);
}
