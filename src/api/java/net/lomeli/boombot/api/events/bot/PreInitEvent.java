package net.lomeli.boombot.api.events.bot;

import java.io.File;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.events.Event;

public class PreInitEvent extends Event {
    private final Addon addon;

    public PreInitEvent(Addon addon) {
        this.addon = addon;
    }

    public Addon getAddon() {
        return addon;
    }

    public File getSuggestedConfigurationFile() {
        return new File("config/" + addon.addonID() + ".cfg");
    }
}