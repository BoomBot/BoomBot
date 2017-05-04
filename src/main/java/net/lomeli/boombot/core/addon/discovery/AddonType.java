package net.lomeli.boombot.core.addon.discovery;

public enum AddonType {
    JAR, CLASS;

    public boolean isJar() {
        return this == JAR;
    }
}
