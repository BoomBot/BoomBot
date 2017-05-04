package net.lomeli.boombot.api.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class Event {
    private boolean canceled;

    /**
     * @return Check if event is cancelled. Only applies if event has
     * {@link net.lomeli.boombot.api.events.Event.Cancelable} annotation
     */
    public final boolean isCanceled() {
        return canceled;
    }

    /**
     * Set if event is cancelled or not. Only applies if event has
     * {@link net.lomeli.boombot.api.events.Event.Cancelable} annotation
     */
    public final void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * Events with this annotation can be cancelled,
     * stopping whatever triggered the event.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Cancelable {
    }

    /**
     * Used to mark functions as event handlers.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface EventHandler {
    }
}
