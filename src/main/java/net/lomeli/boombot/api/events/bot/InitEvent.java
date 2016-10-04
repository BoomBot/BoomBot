package net.lomeli.boombot.api.events.bot;

import net.lomeli.boombot.api.events.Event;

public class InitEvent extends Event {
    private String boomBotID;
    private String boomBotName;
    private String boomBotDiscriminator;

    public InitEvent(String id, String name, String discriminator) {
        this.boomBotID = id;
        this.boomBotName = name;
        this.boomBotDiscriminator = discriminator;
    }

    public String getBoomBotID() {
        return boomBotID;
    }

    public String getBoomBotName() {
        return boomBotName;
    }

    public String getBoomBotDiscriminator() {
        return boomBotDiscriminator;
    }
}
