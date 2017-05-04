package net.lomeli.boombot.api.events.bot;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.events.Event;

/**
 * Fires before BoomBot connects to discord.
 * It's recommended to register eventhandlers
 * during this phase.
 */
public class PreInitEvent extends Event {
    private final Addon addon;

    public PreInitEvent(Addon addon) {
        this.addon = addon;
    }

    public Addon getAddon() {
        return addon;
    }

}