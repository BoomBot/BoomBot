package net.lomeli.boombot.api.events.bot;

import java.io.File;

import net.lomeli.boombot.api.Addon;

public class PreInitEvent {
    private Addon addon;

    public PreInitEvent(Addon addon) {
        this.addon = addon;
    }

    public File getSuggestedConfigurationFile() {
        return new File("config/" + addon.addonID() + ".cfg");
    }
}