package net.lomeli.boombot.api.events;

public interface IEventRegistry {
    void registerEventHandler(Object obj);

    boolean post(Event event);
}
