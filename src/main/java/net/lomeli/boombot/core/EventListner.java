package net.lomeli.boombot.core;

import com.google.common.base.Strings;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.DisconnectEvent;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.ShutdownEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.events.message.priv.GenericPrivateMessageEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.commands.Command;
import net.lomeli.boombot.api.commands.CommandInterface;
import net.lomeli.boombot.api.data.GuildData;
import net.lomeli.boombot.command.custom.CustomRegistry;
import net.lomeli.boombot.command.custom.CustomContent;

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
    public void onGenericGuildMessage(GenericGuildMessageEvent event) {
        if (BoomBot.jda == null || BoomBot.jda.getSelfInfo() == null || event == null || event.getMessage() == null ||
                event.getGuild() == null || event.getAuthor() == null || event.getAuthor().isBot() ||
                (BoomBot.debug && !event.getGuild().getId().equals(BoomBot.debugGuildID) && !event.getChannel().getName().equalsIgnoreCase("test-channel"))) return;
        Message msg = event.getMessage();
        if (msg.isEdited()) return;
        Guild guild = event.getGuild();
        GuildData data = BoomAPI.dataRegistry.getDataForGuild(guild.getId());
        String key = data.getGuildData().getString("commandKey");
        if (msg.getRawContent().startsWith(key)) {
            String rawContent = msg.getRawContent();
            if (Strings.isNullOrEmpty(rawContent)) return;
            String[] splitContent = rawContent.split(" ");
            if (splitContent != null && splitContent.length > 0) {
                String commandName = splitContent[0].substring(1);
                Command cmd = BoomAPI.commandRegistry.getCommand(commandName);
                String message = "";
                if (commandName.length() + 1 < rawContent.length())
                    message = rawContent.substring(commandName.length() + 2);
                CommandInterface cmdInterface = new CommandInterface(event.getAuthor().getId(), guild.getId(),
                        event.getChannel().getId(), message, Strings.isNullOrEmpty(message) ? null : message.split(" "));
                if (cmd != null) {
                    String result = cmd.execute(cmdInterface);
                    if (!Strings.isNullOrEmpty(result)) event.getChannel().sendMessage(result);
                } else {
                    CustomContent custom = CustomRegistry.INSTANCE.getGuildCommand(event.getGuild().getId(), commandName);
                    if (custom != null)
                        event.getChannel().sendMessage(custom.getCommandContent());
                }
            }
        }
    }

    @Override
    public void onGenericPrivateMessage(GenericPrivateMessageEvent event) {
        //event.getJDA().shutdown();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if (event.getGuild() != null) {
            BoomBot.logger.debug("Adding guild {}", event.getGuild().getName());
            if (!BoomAPI.dataRegistry.guildHasData(event.getGuild().getId())) {
                BoomAPI.dataRegistry.addGuild(event.getGuild().getId());
                BoomAPI.dataRegistry.getDataForGuild(event.getGuild().getId()).getGuildData().setString("name", event.getGuild().getName());
            }
        }
    }

    @Override
    public void onDisconnect(DisconnectEvent event) {
        BoomAPI.dataRegistry.writeGuildData();
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        BoomBot.logger.info("BoomBot shutting down!");
        BoomAPI.dataRegistry.writeGuildData();
    }
}
