package net.lomeli.boombot.core.addon.exceptions;

public class WrongBotVersionException extends Exception {
    public WrongBotVersionException(String addonName, String expectedVersion, String installedVersion) {
        super(String.format("Addon %s requesting BoomBot v%, got BoomBot v%s", addonName, expectedVersion, installedVersion));
    }
}
