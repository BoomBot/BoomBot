package net.lomeli.boombot.core.addon.exceptions;

public class DuplicateAddonException extends Exception {
    public DuplicateAddonException(String addonName) {
        super(String.format("Duplicate addons found! Addon ID: %s", addonName));
    }
}
