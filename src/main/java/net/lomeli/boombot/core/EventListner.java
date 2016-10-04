package net.lomeli.boombot.core;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.ShutdownEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.priv.GenericPrivateMessageEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;

public class EventListner extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        if (event.getJDA() != null) {
            List<Guild> guilds = event.getJDA().getGuilds();
            if (guilds != null && !guilds.isEmpty()) {
                for (Guild guild : guilds) {
                    BoomBot.logger.debug("Adding guild {}", guild.getName());
                    BoomAPI.dataRegistry.addGuild(guild.getId());
                    BoomAPI.dataRegistry.getDataForGuild(guild.getId()).getGuildData().setString("name", guild.getName());
                }
            }
        }
    }

    @Override
    public void onGenericPrivateMessage(GenericPrivateMessageEvent event) {
        event.getJDA().shutdown();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (event.getGuild() != null) {
            BoomBot.logger.debug("Adding guild {}", event.getGuild().getName());
            BoomAPI.dataRegistry.addGuild(event.getGuild().getId());
            BoomAPI.dataRegistry.getDataForGuild(event.getGuild().getId()).getGuildData().setString("name", event.getGuild().getName());
        }
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        BoomBot.logger.info("BoomBot shutting down!");
        BoomAPI.dataRegistry.writeGuildData();
    }
}
