package net.lomeli.boombot.api;

import net.dv8tion.jda.entities.Guild;

import java.io.File;

import net.lomeli.boombot.BoomBot;

public class BoomBotAPI {
    public static final File ADDON_FOLDER = new File("addons");
    public static final File ADDON_CONFIG_FOLDER = new File(ADDON_FOLDER, "configs");

    public static File getAddonConfig(String addonID) {
        if (!BoomBotAPI.ADDON_FOLDER.exists() || !BoomBotAPI.ADDON_FOLDER.isDirectory())
            BoomBotAPI.ADDON_FOLDER.mkdir();
        if (!ADDON_CONFIG_FOLDER.exists() || !ADDON_CONFIG_FOLDER.isDirectory())
            ADDON_CONFIG_FOLDER.mkdir();
        return new File(ADDON_CONFIG_FOLDER, addonID);
    }

    public static void sendMessageToGuild(String guildID, String message, Object...args) {
        Guild guild = BoomBot.jda.getGuildById(guildID);
        guild.getPublicChannel().sendMessage(String.format(message, args));
    }
}
