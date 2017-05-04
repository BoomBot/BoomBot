package net.lomeli.boombot.api.registry;

import net.lomeli.boombot.api.events.Event;

public interface IEventRegistry {
    /**
     * Register an object that has an event handler
     *
     * @param obj
     */
    void registerEventHandler(Object obj);

    /**
     * Fire an event
     *
     * @param event
     * @return true if event is cancelable and cancelled.
     */
    boolean post(Event event);
}
