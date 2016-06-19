package net.lomeli.boombot.addons.exceptions;

public class WrongBoomBotVersion extends Exception {
    public WrongBoomBotVersion(String addonName, String expectedVersion, String installedVersion) {
        super(String.format("Addon %s requesting BoomBot v%, got BoomBot v%s", addonName, expectedVersion, installedVersion));
    }
}
